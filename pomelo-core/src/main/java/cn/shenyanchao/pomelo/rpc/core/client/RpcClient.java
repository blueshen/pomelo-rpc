package cn.shenyanchao.pomelo.rpc.core.client;

import cn.shenyanchao.pomelo.rpc.core.client.factory.RpcClientFactory;

/**
 * @author shenyanchao
 */
public interface RpcClient {


    /**
     * 动态调用
     *
     * @param targetInstanceName
     * @param methodName
     * @param argTypes
     * @param args
     * @param timeout
     * @param codecType
     * @param protocolType
     * @return
     * @throws Exception
     */
    Object invokeImpl(String targetInstanceName, String methodName,
                      String[] argTypes, Object[] args, int timeout, int codecType, int protocolType)
            throws Exception;


    /**
     * server address
     *
     * @return String
     */
    String getServerIP();

    /**
     * server port
     *
     * @return int
     */
    int getServerPort();

    /**
     *
     * @return
     */
    RpcClientFactory getRpcClientFactory();

}
