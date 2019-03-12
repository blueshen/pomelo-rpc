package cn.shenyanchao.pomelo.rpc.core.server.handler;

import cn.shenyanchao.pomelo.rpc.core.server.intercepotr.RpcInterceptor;

/**
 * @author shenyanchao
 */
public interface RpcServerHandler {

    /**
     * 添加处理器
     *
     * @param instanceName
     * @param instance
     * @param rpcInterceptor
     */
    void addHandler(String instanceName, Object instance, RpcInterceptor rpcInterceptor);

    /**
     * 清除
     */
    void clear();
}


