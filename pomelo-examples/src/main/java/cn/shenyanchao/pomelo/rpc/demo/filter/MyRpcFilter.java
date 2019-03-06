package cn.shenyanchao.pomelo.rpc.demo.filter;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.shenyanchao.pomelo.rpc.core.server.filter.RpcFilter;

/**
 * @author shenyanchao
 */
public class MyRpcFilter implements RpcFilter {

    private static final Logger LOG = LoggerFactory.getLogger(MyRpcFilter.class);

    @Override
    public boolean doBeforeRequest(Method method, Object processor,
                                   Object[] requestObjects) {

        LOG.info("--doBeforeRequest--");
        return true;
    }

    @Override
    public boolean doAfterRequest(Object processor) {

        LOG.info("--doAfterRequest--");
        return true;
    }

}
