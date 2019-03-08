package cn.shenyanchao.pomelo.rpc.core.client.proxy;

import cn.shenyanchao.pomelo.rpc.serialize.PomeloSerializer;

/**
 * @author shenyanchao
 */
public interface ClientProxy {

    <T> T getProxyService(Class<T> clazz, int timeout, PomeloSerializer serializer,
                          byte protocolType, String targetInstanceName, String group);
}
