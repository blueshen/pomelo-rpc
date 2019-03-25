package cn.shenyanchao.pomelo.rpc.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.inject.Guice;
import com.google.inject.Injector;

import cn.shenyanchao.pomelo.rpc.core.thread.NamedThreadFactory;
import cn.shenyanchao.pomelo.rpc.demo.entity.RpcUser;
import cn.shenyanchao.pomelo.rpc.demo.service.IHelloService;
import cn.shenyanchao.pomelo.rpc.discovery.DiscoveryModule;
import cn.shenyanchao.pomelo.rpc.spring.tcp.support.PomeloRpcApplication;
import cn.shenyanchao.pomelo.rpc.spring.tcp.support.PomeloRpcReference;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.client.handler.PomeloClientProxy;

public class ServiceConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceConsumer.class);

    @Test
    @Ignore
    public void rpcBenchmark() throws Exception {

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "application-client.xml");

        final Injector injector = Guice.createInjector();
        DiscoveryModule discoveryModule = injector.getInstance(DiscoveryModule.class);

        PomeloRpcApplication app = applicationContext.getBean(PomeloRpcApplication.class);
        PomeloRpcReference reference = applicationContext.getBean(PomeloRpcReference.class);
        PomeloClientProxy pomeloClientProxy = injector.getInstance(PomeloClientProxy.class);
        discoveryModule.initZooKeeper(app.getAddress(), app.getTimeout());

        final IHelloService helloService = pomeloClientProxy
                .getProxyService(IHelloService.class, reference.getTimeout(), reference.getSerializer(),
                        reference.getProtocolType()
                        , IHelloService.class.getName(), reference.getGroup());

        // -----------

        long start = System.currentTimeMillis();
        final int count = 10000;
        final int threadCount = 24;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount, new NamedThreadFactory("client"
                + "-send"));
        RpcUser rpcUser = new RpcUser();
        rpcUser.setName("shenyanchao");
        rpcUser.setAge("30");
        List<Future> futureList = new ArrayList<>();
        for (int j = 0; j < threadCount; j++) {
            Future future = executorService.submit(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < count; i++) {
                        String greeting = helloService.sayHiToUser(rpcUser);
                        if (LOG.isDebugEnabled()) {
                            LOG.debug(greeting);
                        }
                    }
                }
            });
            futureList.add(future);
        }
        for (Future future : futureList) {
            future.get();
        }
        long end = System.currentTimeMillis();
        LOG.info("total duration:{}ms, avg cost:{}ms", end - start,
                (end - start) / (double) (count * threadCount));
        LOG.info("qps={}", count * threadCount / ((end - start) / 1000.0));
    }
}
