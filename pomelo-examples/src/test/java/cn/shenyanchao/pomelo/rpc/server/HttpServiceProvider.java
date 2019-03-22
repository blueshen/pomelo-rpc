package cn.shenyanchao.pomelo.rpc.server;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.inject.Guice;
import com.google.inject.Injector;

import cn.shenyanchao.pomelo.rpc.core.server.intercepotr.RpcInterceptor;
import cn.shenyanchao.pomelo.rpc.http.netty4.server.PomeloHttpServer;
import cn.shenyanchao.pomelo.rpc.http.netty4.server.bean.RpcHttpBean;
import cn.shenyanchao.pomelo.rpc.support.PomeloRpcHttpRegistry;
import cn.shenyanchao.pomelo.rpc.support.PomeloRpcHttpService;

/**
 * @author shenyanchao
 */
public class HttpServiceProvider {

    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("application-http.xml");

        PomeloRpcHttpRegistry registry = applicationContext.getBean(PomeloRpcHttpRegistry.class);
        Map<String, PomeloRpcHttpService> serviceMap = applicationContext.getBeansOfType(PomeloRpcHttpService.class);

        final Injector injector = Guice.createInjector();
        PomeloHttpServer pomeloHttpServer = injector.getInstance(PomeloHttpServer.class);
        pomeloHttpServer.run(registry.getPort(), registry.getTimeout());
       for (PomeloRpcHttpService service : serviceMap.values()) {
           Object object = applicationContext.getBean(service.getRef());
           if (StringUtils.isNotBlank(service.getInterceptorRef())) {
               RpcInterceptor rpcInterceptor = (RpcInterceptor) applicationContext.getBean(service.getInterceptorRef());
               RpcHttpBean rpcHttpBean = new RpcHttpBean(object, service.getHttpType(), service.getReturnType());
               pomeloHttpServer.registerService(service.getProjectName(), rpcHttpBean, rpcInterceptor);
           } else {
               RpcHttpBean rpcHttpBean = new RpcHttpBean(object, service.getHttpType(), service.getReturnType());
               pomeloHttpServer.registerService(service.getProjectName(), rpcHttpBean, null);
           }
       }

    }
}
