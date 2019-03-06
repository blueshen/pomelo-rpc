package cn.shenyanchao.pomelo.rpc.core.server.filter;

import java.lang.reflect.Method;

/**
 * @author shenyanchao
 */
public interface RpcFilter {

    /**
     * 拦截服务端开始前处理
     *
     * @param method
     * @param processor
     * @param requestObjects
     *
     * @return
     */
    boolean doBeforeRequest(Method method, Object processor, Object[] requestObjects);

    /**
     * 拦截器服务端后处理
     *
     * @param processor
     *
     * @return
     */
    boolean doAfterRequest(Object processor);

}
