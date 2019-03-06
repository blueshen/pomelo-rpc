package cn.shenyanchao.pomelo.rpc.core.client.factory;

import java.util.concurrent.LinkedBlockingQueue;

import cn.shenyanchao.pomelo.rpc.core.client.AbstractRpcClient;
import cn.shenyanchao.pomelo.rpc.core.client.RpcClient;
import cn.shenyanchao.pomelo.rpc.core.message.PomeloResponseMessage;

/**
 * @author shenyanchao
 */
public interface RpcClientFactory {

    /**
     * @param host
     * @param port
     */
    RpcClient getRpcClient(String host, int port) throws Exception;

    /**
     * 创建并初始化RPC
     *
     * @param connectionTimeout 连接超时时间
     */
    void connect(int connectionTimeout);

    void putResponse(int key, LinkedBlockingQueue<Object> queue) throws Exception;

    /**
     * 接收消息
     *
     * @param response
     *
     * @throws Exception
     */
    void receiveResponse(PomeloResponseMessage response) throws Exception;

    /**
     * 删除消息
     *
     * @param key
     */
    void removeResponse(int key);

    /**
     * @param host
     * @param port
     * @param rpcClient
     */
    void putRpcClient(String host, int port, AbstractRpcClient rpcClient);

    /**
     * @param host
     * @param port
     */
    void removeRpcClient(String host, int port);

    /**
     * @param host
     * @param port
     *
     * @return
     */
    boolean containClient(String host, int port);

}
