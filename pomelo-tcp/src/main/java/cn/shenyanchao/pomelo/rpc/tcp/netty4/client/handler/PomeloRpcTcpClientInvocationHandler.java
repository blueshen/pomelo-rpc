package cn.shenyanchao.pomelo.rpc.tcp.netty4.client.handler;

import cn.shenyanchao.pomelo.rpc.core.client.factory.RpcClientFactory;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.client.factory.PomeloRpcTcpClientFactory;

/**
 * @author shenyanchao
 */
public class PomeloRpcTcpClientInvocationHandler extends AbstractRpcClientInvocationHandler {

    public PomeloRpcTcpClientInvocationHandler(String group,
                                               int timeout, String targetInstanceName,
                                               int codecType, int protocolType) {
        super(group, timeout, targetInstanceName, codecType, protocolType);
    }

    @Override
    public RpcClientFactory getClientFactory() {
        return PomeloRpcTcpClientFactory.getInstance();
    }

}
