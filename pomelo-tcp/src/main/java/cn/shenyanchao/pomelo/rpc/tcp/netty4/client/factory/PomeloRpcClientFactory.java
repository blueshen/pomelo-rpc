package cn.shenyanchao.pomelo.rpc.tcp.netty4.client.factory;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cn.shenyanchao.pomelo.rpc.core.protocol.PomeloRpcProtocol;
import cn.shenyanchao.pomelo.rpc.core.thread.NamedThreadFactory;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.client.ClientHolder;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.client.PomeloRpcClient;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.client.ResponseModule;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.client.RpcClient;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.client.handler.PomeloTcpClientHandler;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.server.handler.PomeloRpcDecoderHandler;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.server.handler.PomeloRpcEncoderHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author shenyanchao
 */
@Singleton
public class PomeloRpcClientFactory implements RpcClientFactory {

    private static final Logger LOG = LoggerFactory.getLogger(PomeloRpcClientFactory.class);
    private static final int PROCESSORS = Runtime.getRuntime().availableProcessors();
    private static final ThreadFactory workerThreadFactory = new NamedThreadFactory("pomelo-worker-");
    private static final int CONNECT_TIMEOUT = 1000;
    private static EventLoopGroup workerGroup = new NioEventLoopGroup(2 * PROCESSORS, workerThreadFactory);
    private final Bootstrap bootstrap = new Bootstrap();

    @Inject
    private ResponseModule responseModule;

    @Inject
    private ClientHolder clientHolder;

    @Inject
    private PomeloRpcProtocol pomeloRpcProtocol;

    public PomeloRpcClientFactory() {
        LOG.debug("---------------- pomelo client connect ----------------");
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_SNDBUF, 65535)
                .option(ChannelOption.SO_RCVBUF, 65535);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) {
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast("decoder", new PomeloRpcDecoderHandler(pomeloRpcProtocol));
                pipeline.addLast("encoder", new PomeloRpcEncoderHandler(pomeloRpcProtocol));
                pipeline.addLast("timeout", new IdleStateHandler(0, 0, 120));
                pipeline.addLast("handler", new PomeloTcpClientHandler(clientHolder, responseModule));
            }

        });
        LOG.debug("---------------- pomelo client start success ------------------");
    }

    @Override
    public RpcClient getRpcClient(String targetIP, int targetPort) throws Exception {
        if (!clientHolder.containClient(targetIP, targetPort)) {
            RpcClient rpcClient = createClient(targetIP, targetPort);
            clientHolder.putRpcClient(targetIP, targetPort, rpcClient);
        }

        return clientHolder.getRpcClient(targetIP, targetPort);
    }

    private RpcClient createClient(String targetIP, int targetPort) throws Exception {
        ChannelFuture future =
                bootstrap.connect(new InetSocketAddress(targetIP, targetPort)).sync();
        future.awaitUninterruptibly();
        if (!future.isDone()) {
            LOG.error("Create connection to {}:{} timeout!", targetIP, targetIP);
            throw new Exception("Create connection to " + targetIP + ":" + targetPort + " timeout!");
        }
        if (future.isCancelled()) {
            LOG.error("Create connection to {}:{} cancelled by user!", targetIP, targetIP);
            throw new Exception("Create connection to " + targetIP + ":" + targetPort + " cancelled by user!");
        }
        if (!future.isSuccess()) {
            LOG.error("Create connection to {}:{} error!", targetIP, targetIP, future.cause());
            throw new Exception("Create connection to " + targetIP + ":" + targetPort + " error", future.cause());
        }
        PomeloRpcClient client = new PomeloRpcClient(future, responseModule, clientHolder);
        return client;
    }

    public ResponseModule getResponseModule() {
        return responseModule;
    }

    public ClientHolder getClientHolder() {
        return clientHolder;
    }
}
