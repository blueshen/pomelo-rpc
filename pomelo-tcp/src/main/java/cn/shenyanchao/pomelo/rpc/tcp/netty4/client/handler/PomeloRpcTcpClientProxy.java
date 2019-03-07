package cn.shenyanchao.pomelo.rpc.tcp.netty4.client.handler;

import java.lang.reflect.Proxy;

import cn.shenyanchao.pomelo.rpc.core.client.proxy.ClientProxy;

/**
 * @author shenyanchao
 */
public class PomeloRpcTcpClientProxy implements ClientProxy {

    public PomeloRpcTcpClientProxy() {

    }

    public static PomeloRpcTcpClientProxy getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public <T> T getProxyService(Class<T> clazz, int timeout, int serializerType,
                                 int protocolType, String targetInstanceName, String group) {
        return (T) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[] {clazz},
                new PomeloRpcTcpClientInvocationHandler(group, timeout,
                        targetInstanceName, serializerType, protocolType));
    }

    private static class SingletonHolder {
        static final PomeloRpcTcpClientProxy instance = new PomeloRpcTcpClientProxy();
    }
}
