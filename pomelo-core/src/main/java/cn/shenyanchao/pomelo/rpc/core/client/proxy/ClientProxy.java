package cn.shenyanchao.pomelo.rpc.core.client.proxy;

import cn.shenyanchao.pomelo.rpc.serialize.PomeloSerializer;

/**
 * 客户端代理
 *
 * @author shenyanchao
 */
public interface ClientProxy {

    /**
     * 代理类
     * @param clazz
     * @param timeout
     * @param serializer
     * @param protocolType
     * @param targetInstanceName
     * @param group
     * @param <T>
     * @return
     */
    <T> T getProxyService(Class<T> clazz, int timeout, PomeloSerializer serializer,
                          byte protocolType, String targetInstanceName, String group);
}
