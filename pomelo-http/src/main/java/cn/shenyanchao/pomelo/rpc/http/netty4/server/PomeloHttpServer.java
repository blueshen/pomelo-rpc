package cn.shenyanchao.pomelo.rpc.http.netty4.server;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.shenyanchao.pomelo.rpc.core.server.RpcServer;
import cn.shenyanchao.pomelo.rpc.core.server.filter.RpcInterceptor;
import cn.shenyanchao.pomelo.rpc.core.thread.NamedThreadFactory;
import cn.shenyanchao.pomelo.rpc.http.RpcHttpServerHandler;
import cn.shenyanchao.pomelo.rpc.http.netty4.server.handler.PomeloHttpServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author shenyanchao
 */
public class PomeloHttpServer implements RpcServer {

    private static final Logger LOG = LoggerFactory.getLogger(PomeloHttpServer.class);

    private static final int PROCESSORS = Runtime.getRuntime().availableProcessors();

    private EventLoopGroup bossGroup;

    private NioEventLoopGroup workerGroup;

    private PomeloHttpServer() {

    }

    public static PomeloHttpServer getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public void registerService(String serviceName, Object serviceInstance,
                                RpcInterceptor rpcFilter) {

        RpcHttpServerHandler.getInstance().addHandler(serviceName, serviceInstance, rpcFilter);
    }

    @Override
    public void stop() {
        RpcHttpServerHandler.getInstance().clear();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    @Override
    public void run(int port, final int timeout) throws Exception {

        ThreadFactory serverBossTF = new NamedThreadFactory("pomelo-http-io-");
        ThreadFactory serverWorkerTF = new NamedThreadFactory("pomelo-http-worker-");
        bossGroup = new NioEventLoopGroup(PROCESSORS, serverBossTF);
        workerGroup = new NioEventLoopGroup(PROCESSORS * 2, serverWorkerTF);
        workerGroup.setIoRatio(50);
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_SNDBUF, 65535)
                .option(ChannelOption.SO_RCVBUF, 65535)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast("codec", new HttpServerCodec());
                pipeline.addLast("aggregator", new HttpObjectAggregator(512 * 1024));
                pipeline.addLast("timeout", new IdleStateHandler(0, 0, 120));
                pipeline.addLast("biz", new PomeloHttpServerHandler());
            }

        });
        LOG.info("-----------------pomelo starting--------------------------");
        bootstrap.bind(new InetSocketAddress(port)).sync();
        LOG.info("http server stared on：{}", port);
        LOG.info("-----------------start success!--------------------------");
    }

    private static class SingletonHolder {
        static final PomeloHttpServer instance = new PomeloHttpServer();
    }

}
