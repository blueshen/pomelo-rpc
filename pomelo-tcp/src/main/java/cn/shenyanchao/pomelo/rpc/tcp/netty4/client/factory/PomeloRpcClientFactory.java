package cn.shenyanchao.pomelo.rpc.tcp.netty4.client.factory;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.shenyanchao.pomelo.rpc.core.client.RpcClient;
import cn.shenyanchao.pomelo.rpc.core.client.factory.AbstractRpcClientFactory;
import cn.shenyanchao.pomelo.rpc.core.thread.NamedThreadFactory;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.client.PomeloRpcClient;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.client.handler.PomeloTcpClientHandler;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.codec.PomeloRpcDecoderHandler;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.codec.PomeloRpcEncoderHandler;
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
public class PomeloRpcClientFactory extends AbstractRpcClientFactory {

    private static final Logger LOG = LoggerFactory.getLogger(PomeloRpcClientFactory.class);
    private static final int PROCESSORS = Runtime.getRuntime().availableProcessors();
    private static final ThreadFactory workerThreadFactory = new NamedThreadFactory("pomelo-worker-");
    private static EventLoopGroup workerGroup = new NioEventLoopGroup(6 * PROCESSORS, workerThreadFactory);

    private final Bootstrap bootstrap = new Bootstrap();

    public static PomeloRpcClientFactory getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public void connect(int connectTimeout) {
        LOG.info("----------------client connect -------------------------------");
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_SNDBUF, 65535)
                .option(ChannelOption.SO_RCVBUF, 65535);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast("decoder", new PomeloRpcDecoderHandler());
                pipeline.addLast("encoder", new PomeloRpcEncoderHandler());
                pipeline.addLast("timeout", new IdleStateHandler(0, 0, 120));
                pipeline.addLast("handler", new PomeloTcpClientHandler());
            }

        });
        LOG.info("---------------- client start success-------------------------------");
    }

    @Override
    protected RpcClient createClient(String targetIP, int targetPort) throws Exception {
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(targetIP, targetPort)).sync();
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
        PomeloRpcClient client = new PomeloRpcClient(future);
        putRpcClient(targetIP, targetPort, client);
        return client;
    }

    @Override
    public void stopClient() throws Exception {
        getInstance().clearClients();
        workerGroup.shutdownGracefully();
    }

    static class SingletonHolder {
        public static PomeloRpcClientFactory instance = new PomeloRpcClientFactory();
    }

}
