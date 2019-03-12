package cn.shenyanchao.pomelo.rpc.tcp.netty4.client.factory;

/**
 * @author shenyanchao
 */
public interface RpcClientFactory {

    /**
     * @param host
     * @param port
     */
    RpcClient getRpcClient(String host, int port) throws Exception;

}
