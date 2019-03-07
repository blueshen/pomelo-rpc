package cn.shenyanchao.pomelo.rpc.tcp.netty4.server.handler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.shenyanchao.pomelo.rpc.core.message.PomeloRequestMessage;
import cn.shenyanchao.pomelo.rpc.core.message.PomeloResponseMessage;
import cn.shenyanchao.pomelo.rpc.core.thread.NamedThreadFactory;
import cn.shenyanchao.pomelo.rpc.util.NetUtils;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @author shenyanchao
 */
public class PomeloRpcTcpServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(PomeloRpcTcpServerHandler.class);

    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * 协议名称
     */
    private int protocolType;

    /**
     * 编码类型
     */
    private int serializerType;

    public PomeloRpcTcpServerHandler(int threadCount, int protocolType, int serializerType) {
        super();
        this.protocolType = protocolType;
        this.serializerType = serializerType;
        threadPoolExecutor = new ThreadPoolExecutor(threadCount, threadCount,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(), new NamedThreadFactory("rpc-handler"));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String remoteAddress = ctx.channel().remoteAddress().toString();
        if (LOG.isDebugEnabled()) {
            LOG.debug("remote IP:{}", remoteAddress);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e)
            throws Exception {
        if (!(e.getCause() instanceof IOException)) {
            LOG.error("catch some exception not IOException", e);
        }
        ctx.channel().close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        if (!(msg instanceof PomeloRequestMessage)) {
            LOG.error("receive message error,only support RequestWrapper");
            throw new Exception(
                    "receive message error,only support RequestWrapper || List");
        }
        threadPoolExecutor.submit(new ServerHandlerRunnable(ctx, msg));
    }

    /**
     * disruptor处理方式
     *
     * @param ctx
     * @param message
     */
    private void handleRequestWithSingleThread(final ChannelHandlerContext ctx, Object message) {
        PomeloResponseMessage pomeloResponseMessage;
        try {
            PomeloRequestMessage request = (PomeloRequestMessage) message;
            pomeloResponseMessage =
                    RpcTcpServerHandler.getInstance().handleRequest(request, serializerType, protocolType);
            if (ctx.channel().isOpen()) {
                ChannelFuture wf = ctx.channel().writeAndFlush(pomeloResponseMessage);
                wf.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (!future.isSuccess()) {

                            LOG.error(
                                    "server write response error,client  host is: {}:{},server Ip:{}",
                                    ((InetSocketAddress) ctx.channel().remoteAddress()).getHostName(),
                                    ((InetSocketAddress) ctx.channel().remoteAddress()).getPort(), getLocalhost());
                            ctx.channel().close();
                        }
                    }
                });
            }

        } catch (Exception e) {
            sendErrorResponse(ctx, (PomeloRequestMessage) message, e.getMessage() + ",server Ip:" + getLocalhost());
        } finally {
            ReferenceCountUtil.release(message);
        }
    }

    private void sendErrorResponse(final ChannelHandlerContext ctx, final PomeloRequestMessage request,
                                   String errorMessage) {
        PomeloResponseMessage commonRpcResponse =
                new PomeloResponseMessage(request.getId(), request.getSerializerType(), request.getProtocolType());
        commonRpcResponse.setException(new Exception(errorMessage));
        ChannelFuture wf = ctx.channel().writeAndFlush(commonRpcResponse);

        wf.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    LOG.error("server write response error,request id is: {},client Ip is: {},server Ip:{}",
                            request.getId(), ctx
                                    .channel().remoteAddress().toString(), getLocalhost());
                    ctx.channel().close();
                }
            }
        });
    }

    private String getLocalhost() {
        try {
            return NetUtils.getLocalHost();
        } catch (Exception e) {
            throw new RuntimeException("can not find localhost ip", e);
        }
    }

    private class ServerHandlerRunnable implements Runnable {

        private ChannelHandlerContext ctx;

        private Object message;

        /**
         * @param ctx
         * @param message
         */
        public ServerHandlerRunnable(ChannelHandlerContext ctx, Object message) {
            super();
            this.ctx = ctx;
            this.message = message;
        }

        @Override
        public void run() {
            handleRequestWithSingleThread(ctx, message);
        }

    }
}
