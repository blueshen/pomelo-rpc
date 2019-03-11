package cn.shenyanchao.pomelo.rpc.core.server.intercepotr;

import java.lang.reflect.Method;

/**
 * @author shenyanchao
 */
public interface RpcInterceptor {

    /**
     * 拦截服务端开始前处理
     *
     * @param method
     * @param processor
     * @param requestObjects
     *
     * @return true or false
     */
    boolean before(Method method, Object processor, Object[] requestObjects);

    /**
     * 拦截器服务端后处理
     *
     * @param processor
     *
     * @return true or false
     */
    boolean after(Object processor);

}
