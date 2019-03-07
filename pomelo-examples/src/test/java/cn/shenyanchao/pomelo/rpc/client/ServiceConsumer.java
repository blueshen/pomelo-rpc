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

import cn.shenyanchao.pomelo.rpc.core.thread.NamedThreadFactory;
import cn.shenyanchao.pomelo.rpc.demo.entity.RpcUser;
import cn.shenyanchao.pomelo.rpc.demo.service.IHelloService;

public class ServiceConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceConsumer.class);

    @Test
    @Ignore
    public void rpcBenchmark() throws Exception {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                "application-client.xml");
        final IHelloService helloService = (IHelloService) context
                .getBean("helloService");
        long start = System.currentTimeMillis();
        final int count = 10000;
        final int threadcount = 24;

        ExecutorService executorService = Executors.newFixedThreadPool(threadcount, new NamedThreadFactory("client"
                + "-send"));
        RpcUser rpcUser = new RpcUser();
        rpcUser.setName("shenyanchao");
        rpcUser.setAge("30");
        List<Future> futureList = new ArrayList<>();
        for (int j = 0; j < threadcount; j++) {
            Future future = executorService.submit(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < count; i++) {
                        String greeting = helloService.sayHiToUser(rpcUser);
                        //                        LOG.info(greeting);
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
                (end - start) / (double) (count * threadcount));
        LOG.info("qps={}", count * threadcount / ((end - start) / 1000.0));
    }
}
