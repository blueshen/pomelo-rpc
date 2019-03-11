package cn.shenyanchao.pomelo.rpc.demo.filter;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.shenyanchao.pomelo.rpc.core.server.intercepotr.RpcInterceptor;

/**
 * @author shenyanchao
 */
public class MyRpcInterceptor implements RpcInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(MyRpcInterceptor.class);

    @Override
    public boolean before(Method method, Object processor,
                          Object[] requestObjects) {

        //        LOG.info("--before interceptor--");
        return true;
    }

    @Override
    public boolean after(Object processor) {

        //        LOG.info("--after interceptor--");
        return true;
    }

}
