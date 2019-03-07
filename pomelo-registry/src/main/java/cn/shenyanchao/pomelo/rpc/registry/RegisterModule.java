package cn.shenyanchao.pomelo.rpc.registry;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceInstanceBuilder;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shenyanchao
 */
public class RegisterModule implements IRegisterModule {

    public static final String BASE_PATH = "/pomelo";
    private static final Logger LOG = LoggerFactory.getLogger(RegisterModule.class);
    private CuratorFramework client;
    private ServiceDiscovery serviceDiscovery;

    private RegisterModule() {

    }

    public static RegisterModule getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public void close() throws Exception {
        if (null != serviceDiscovery) {
            serviceDiscovery.close();
        }
        if (null != client) {
            client.close();
        }
    }

    @Override
    public void initZooKeeper(String zkServer, int timeout) throws Exception {
        client = CuratorFrameworkFactory.builder()
                .connectString(zkServer)
                .connectionTimeoutMs(timeout)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();
        client.blockUntilConnected();
        LOG.info("connect to zkServer : {} success!", zkServer);

    }

    @Override
    public void registerServer(String group, String server, int port) throws Exception {

        ServiceInstanceBuilder<String> sib = ServiceInstance.builder();
        sib.address(server);
        sib.port(port);
        sib.name(group);
        sib.payload("shenyanchao");

        ServiceInstance<String> instance = sib.build();

        serviceDiscovery = ServiceDiscoveryBuilder.builder(String.class)
                .client(client)
                .serializer(new JsonInstanceSerializer<>(String.class))
                .basePath(BASE_PATH)
                .build();
        //服务注册
        serviceDiscovery.registerService(instance);
        serviceDiscovery.start();
    }

    private static class SingletonHolder {
        static final RegisterModule instance = new RegisterModule();
    }

}
