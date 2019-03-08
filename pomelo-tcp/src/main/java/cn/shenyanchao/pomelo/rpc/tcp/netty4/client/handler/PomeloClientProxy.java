package cn.shenyanchao.pomelo.rpc.tcp.netty4.client.handler;

import java.lang.reflect.Proxy;

import cn.shenyanchao.pomelo.rpc.core.client.proxy.ClientProxy;
import cn.shenyanchao.pomelo.rpc.serialize.PomeloSerializer;

/**
 * @author shenyanchao
 */
public class PomeloClientProxy implements ClientProxy {

    private PomeloClientProxy() {

    }

    public static PomeloClientProxy getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public <T> T getProxyService(Class<T> clazz, int timeout, PomeloSerializer serializer,
                                 byte protocolType, String targetInstanceName, String group) {
        return (T) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[] {clazz},
                new PomeloRpcTcpClientInvocationHandler(group, timeout,
                        targetInstanceName, serializer, protocolType));
    }

    private static class SingletonHolder {
        static final PomeloClientProxy instance = new PomeloClientProxy();
    }
}
