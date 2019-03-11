package cn.shenyanchao.pomelo.rpc.discovery;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceCache;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import cn.shenyanchao.pomelo.rpc.registry.RegisterModule;

/**
 * @author shenyanchao
 */

@Singleton
public class DiscoveryModule implements IDiscoveryModule {

    private static final Logger LOG = LoggerFactory.getLogger(DiscoveryModule.class);
    private static Map<String, Set<InetSocketAddress>> servers = new ConcurrentHashMap<>();
    private CuratorFramework client;
    private ServiceDiscovery serviceDiscovery;
    private ServiceCache serviceCache;

    /**
     * 方法置为同步，解决serviceCache多次start问题
     *
     * @param group
     *
     * @return
     *
     * @throws Exception
     */
    @Override
    public Set<InetSocketAddress> getActiveServersByGroup(final String group) throws Exception {
        synchronized(this) {
            if (null == serviceDiscovery) {
                serviceDiscovery = ServiceDiscoveryBuilder.builder(String.class)
                        .client(client)
                        .basePath(RegisterModule.BASE_PATH)
                        .build();
                serviceDiscovery.start();
            }
            if (null == serviceCache) {
                serviceCache = serviceDiscovery.serviceCacheBuilder().name(group).build();
                serviceCache.start();
            }
        }
        Collection<ServiceInstance<String>> services = serviceCache.getInstances();

        Set<InetSocketAddress> addressList = new HashSet<>();
        for (ServiceInstance<String> service : services) {
            InetSocketAddress socketAddress = new InetSocketAddress(service.getAddress(), service.getPort());
            addressList.add(socketAddress);
        }
        servers.put(group, addressList);
        if (LOG.isDebugEnabled()) {
            LOG.debug("[{group}] active server ip:{}", group, addressList);
        }
        return servers.get(group);

    }

    @Override
    public void close() throws Exception {
        if (null != serviceCache) {
            serviceCache.close();
        }
        if (null != serviceDiscovery) {
            serviceDiscovery.close();
        }
        servers.clear();
        if (null != client) {
            client.close();
        }
    }

    @Override
    public void initZooKeeper(final String server, final int timeout) throws Exception {
        client = CuratorFrameworkFactory.builder()
                .connectString(server)
                .connectionTimeoutMs(timeout)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();
        client.blockUntilConnected();
        LOG.info("client connect to server success");

    }

}
