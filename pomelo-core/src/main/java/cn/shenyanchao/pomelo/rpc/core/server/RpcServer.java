package cn.shenyanchao.pomelo.rpc.core.server;

import cn.shenyanchao.pomelo.rpc.core.server.filter.RpcInterceptor;

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
    void registerService(String serviceName, Object serviceInstance, RpcInterceptor rpcFilter);

    /**
     * 运行
     * @param port 端口
     * @param timeout 超时时间
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
