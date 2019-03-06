package cn.shenyanchao.pomelo.rpc.core.server;

import cn.shenyanchao.pomelo.rpc.core.server.filter.RpcFilter;

/**
 * @author shenyanchao
 */
public interface RpcServer {

    /**
     * 注册服务
     *
     * @param serviceName     服务名称
     * @param serviceInstance 服务实例
     */
    void registerService(String serviceName, Object serviceInstance, RpcFilter rpcFilter);

    /**
     * @param port
     * @param timeout
     *
     * @throws Exception
     */
    void run(int port, int timeout) throws Exception;

    /**
     * 停止
     *
     * @throws Exception
     */
    void stop() throws Exception;
}
