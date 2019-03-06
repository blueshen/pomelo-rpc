package cn.shenyanchao.pomelo.rpc.core.client.proxy;

/**
 * @author shenyanchao
 */
public interface ClientProxy {

    <T> T getProxyService(Class<T> clazz, int timeout, int codecType,
                          int protocolType, String targetInstanceName, String group);
}
