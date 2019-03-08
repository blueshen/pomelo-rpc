package cn.shenyanchao.pomelo.rpc.tcp.netty4.client.handler;

import cn.shenyanchao.pomelo.rpc.core.client.factory.RpcClientFactory;
import cn.shenyanchao.pomelo.rpc.serialize.PomeloSerializer;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.client.factory.PomeloRpcClientFactory;

/**
 * @author shenyanchao
 */
public class PomeloRpcTcpClientInvocationHandler extends AbstractRpcClientInvocationHandler {

    public PomeloRpcTcpClientInvocationHandler(String group,
                                               int timeout, String targetInstanceName,
                                               PomeloSerializer serializer, byte protocolType) {
        super(group, timeout, targetInstanceName,  serializer, protocolType);
    }

    @Override
    public RpcClientFactory getClientFactory() {
        return PomeloRpcClientFactory.getInstance();
    }

}
