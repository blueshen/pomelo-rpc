package cn.shenyanchao.pomelo.rpc.tcp.netty4.server.handler;

import cn.shenyanchao.pomelo.rpc.core.message.PomeloRequestMessage;
import cn.shenyanchao.pomelo.rpc.core.message.PomeloResponseMessage;
import cn.shenyanchao.pomelo.rpc.core.server.handler.RpcServerHandler;
import cn.shenyanchao.pomelo.rpc.serialize.PomeloSerializer;

/**
 * @author shenyanchao
 */
public abstract class AbstractRpcTcpServerHandler implements RpcServerHandler {

    /**
     * 处理请求
     *
     * @param request
     * @param serializer
     * @param protocolType
     *
     * @return
     */
    public abstract PomeloResponseMessage handleRequest(PomeloRequestMessage request, PomeloSerializer serializer,
                                                        byte protocolType);

}
