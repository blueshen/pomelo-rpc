package cn.shenyanchao.pomelo.rpc.tcp.netty4.client.handler;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.shenyanchao.pomelo.rpc.core.message.PomeloResponseMessage;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.client.factory.PomeloRpcTcpClientFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @author shenyanchao
 */
public class PomeloTcpClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(PomeloTcpClientHandler.class);

    public PomeloTcpClientHandler() {
        super();

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg instanceof PomeloResponseMessage) {
                PomeloResponseMessage response = (PomeloResponseMessage) msg;
                if (LOG.isDebugEnabled()) {
                    LOG.debug("receive response list from server: {} ,requestId is: {}", ctx.channel().remoteAddress(),
                            response.getRequestId());
                }
                PomeloRpcTcpClientFactory.getInstance().receiveResponse(response);
            } else {
                LOG.error("receive message error,only support List || ResponseWrapper");
                throw new Exception(
                        "receive message error,only support List || ResponseWrapper");
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
        if (!(e.getCause() instanceof IOException)) {
            LOG.error("catch some exception not IOException", e);
        }
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        PomeloRpcTcpClientFactory.getInstance().removeRpcClient(remoteAddress.getHostName(), remoteAddress.getPort());
        if (ctx.channel().isOpen()) {
            ctx.channel().close();
        }

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        LOG.error("connection closed: {} ", ctx.channel().remoteAddress());
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        PomeloRpcTcpClientFactory.getInstance().removeRpcClient(remoteAddress.getHostName(), remoteAddress.getPort());
        if (ctx.channel().isOpen()) {
            ctx.channel().close();
        }
    }

}
