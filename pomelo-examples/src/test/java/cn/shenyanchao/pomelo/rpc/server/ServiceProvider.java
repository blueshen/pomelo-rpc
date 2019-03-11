package cn.shenyanchao.pomelo.rpc.server;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.inject.Guice;
import com.google.inject.Injector;

import cn.shenyanchao.pomelo.rpc.core.server.filter.RpcInterceptor;
import cn.shenyanchao.pomelo.rpc.registry.RegisterModule;
import cn.shenyanchao.pomelo.rpc.support.PomeloRpcApplication;
import cn.shenyanchao.pomelo.rpc.support.PomeloRpcRegistry;
import cn.shenyanchao.pomelo.rpc.support.PomeloRpcService;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.server.PomeloTcpServer;

/**
 * @author shenyanchao
 */
public class ServiceProvider {

    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("application-rpc.xml");

        PomeloRpcApplication app = applicationContext.getBean(PomeloRpcApplication.class);
        PomeloRpcRegistry registry = applicationContext.getBean(PomeloRpcRegistry.class);
        PomeloRpcService service = applicationContext.getBean(PomeloRpcService.class);

        final Injector injector = Guice.createInjector();
        PomeloTcpServer pomeloTcpServer = injector.getInstance(PomeloTcpServer.class);
        RegisterModule registerModule = injector.getInstance(RegisterModule.class);
        registerModule.initZooKeeper(app.getAddress(), app.getTimeout());

        pomeloTcpServer.setSerializer(registry.getSerializer());
        pomeloTcpServer.setProtocolType(registry.getProtocolType());
        pomeloTcpServer.setThreadCount(registry.getThreadCount());
        pomeloTcpServer.run(registry.getPort(), registry.getTimeout());
        // 注册服务节点
        registerModule.registerServer(registry.getGroup(), registry.getLocalhost(), registry.getPort());

        if (StringUtils.isBlank(service.getInterceptorRef()) ||
                !(applicationContext.getBean(service.getInterceptorRef()) instanceof RpcInterceptor)) {
            pomeloTcpServer
                    .registerService(service.getInterfaceName(), applicationContext.getBean(service.getRef()), null);
        } else {
            pomeloTcpServer.registerService(service.getInterfaceName(), applicationContext.getBean(service.getRef()),
                    (RpcInterceptor) applicationContext.getBean(service.getInterceptorRef()));
        }

    }
}
