package cn.shenyanchao.pomelo.rpc.core.server.handler;

import cn.shenyanchao.pomelo.rpc.core.server.filter.RpcFilter;

/**
 * @author shenyanchao
 */
public interface RpcServerHandler {

    /**
     * 添加处理器
     *
     * @param instanceName
     * @param instance
     */
    void addHandler(String instanceName, Object instance, RpcFilter rpcFilter);

    /**
     * 清除
     */
    void clear();
}
