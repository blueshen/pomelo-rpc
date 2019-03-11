package cn.shenyanchao.pomelo.rpc.tcp.netty4.client.handler;

import java.lang.reflect.Proxy;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cn.shenyanchao.pomelo.rpc.core.client.proxy.ClientProxy;
import cn.shenyanchao.pomelo.rpc.discovery.DiscoveryModule;
import cn.shenyanchao.pomelo.rpc.serialize.PomeloSerializer;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.client.factory.PomeloRpcClientFactory;

/**
 * @author shenyanchao
 */

@Singleton
public class PomeloClientProxy implements ClientProxy {

    @Inject
    private DiscoveryModule discoveryModule;

    @Inject
    private PomeloRpcClientFactory pomeloRpcClientFactory;

    @Override
    public <T> T getProxyService(Class<T> clazz, int timeout, PomeloSerializer serializer,
                                 byte protocolType, String targetInstanceName, String group) {

        return (T) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[] {clazz},
                new PomeloRpcTcpClientInvocationHandler(group, timeout,
                        targetInstanceName, serializer, protocolType, discoveryModule, pomeloRpcClientFactory));
    }

}
