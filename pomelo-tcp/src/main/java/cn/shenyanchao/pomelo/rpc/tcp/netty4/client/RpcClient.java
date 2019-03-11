package cn.shenyanchao.pomelo.rpc.tcp.netty4.client;

import cn.shenyanchao.pomelo.rpc.serialize.PomeloSerializer;

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
     * @param serializer
     * @param protocolType
     *
     * @return
     *
     * @throws Exception
     */
    Object invokeImpl(String targetInstanceName, String methodName,
                      String[] argTypes, Object[] args, int timeout, PomeloSerializer serializer, byte protocolType)
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


}
