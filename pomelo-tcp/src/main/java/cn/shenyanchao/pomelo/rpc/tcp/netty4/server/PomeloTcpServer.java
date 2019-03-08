package cn.shenyanchao.pomelo.rpc.tcp.netty4.server;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.shenyanchao.pomelo.rpc.core.server.RpcServer;
import cn.shenyanchao.pomelo.rpc.core.server.filter.RpcInterceptor;
import cn.shenyanchao.pomelo.rpc.serialize.PomeloSerializer;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.server.handler.RpcTcpServerHandler;
import cn.shenyanchao.pomelo.rpc.core.thread.NamedThreadFactory;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.codec.PomeloRpcDecoderHandler;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.codec.PomeloRpcEncoderHandler;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.server.handler.PomeloRpcTcpServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollMode;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * TcpServer 实现
 *
 * @author shenyanchao
 */
public class PomeloTcpServer implements RpcServer {

    private static final Logger LOG = LoggerFactory.getLogger(PomeloTcpServer.class);

    private static final int PROCESSORS = Runtime.getRuntime().availableProcessors();

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    /**
     * 协议名称
     */
    private byte protocolType;

    /**
     * 序列化类型
     */
    private PomeloSerializer serializer;

    /**
     * 线程数
     */
    private int threadCount;

    private PomeloTcpServer() {

    }

    public static PomeloTcpServer getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public void registerService(String serviceName, Object serviceInstance, RpcInterceptor rpcFilter) {
        RpcTcpServerHandler.getInstance().addHandler(serviceName, serviceInstance, rpcFilter);
    }

    @Override
    public void stop() {
        RpcTcpServerHandler.getInstance().clear();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    @Override
    public void run(final int port, final int timeout) throws Exception {
        ThreadFactory serverBossTF = new NamedThreadFactory("pomelo-io-");
        ThreadFactory serverWorkerTF = new NamedThreadFactory("pomelo-worker-");

        ServerBootstrap bootstrap = new ServerBootstrap();
        if (Epoll.isAvailable()) {
            bossGroup = new EpollEventLoopGroup(PROCESSORS, serverBossTF);
            workerGroup = new EpollEventLoopGroup(PROCESSORS * 2, serverWorkerTF);
            ((EpollEventLoopGroup) bossGroup).setIoRatio(100);
            ((EpollEventLoopGroup) workerGroup).setIoRatio(100);
            bootstrap.channel(EpollServerSocketChannel.class);
            bootstrap.option(EpollChannelOption.EPOLL_MODE, EpollMode.EDGE_TRIGGERED);
            bootstrap.childOption(EpollChannelOption.EPOLL_MODE, EpollMode.EDGE_TRIGGERED);
            LOG.info("----- USE EPoll-------");
        } else {
            bossGroup = new NioEventLoopGroup(PROCESSORS, serverBossTF);
            workerGroup = new NioEventLoopGroup(PROCESSORS * 2, serverWorkerTF);
            ((NioEventLoopGroup) bossGroup).setIoRatio(100);
            ((NioEventLoopGroup) workerGroup).setIoRatio(100);
            bootstrap.channel(NioServerSocketChannel.class);
        }
        bootstrap.group(bossGroup, workerGroup)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_SNDBUF, 65535)
                .option(ChannelOption.SO_RCVBUF, 65535)
                .childOption(ChannelOption.TCP_NODELAY, true);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast("decoder", new PomeloRpcDecoderHandler());
                pipeline.addLast("encoder", new PomeloRpcEncoderHandler());
                pipeline.addLast("timeout", new IdleStateHandler(0, 0, 120));
                pipeline.addLast("handler", new PomeloRpcTcpServerHandler(threadCount, protocolType, serializer));

            }

        });
        LOG.info("----------- pomelo starting ------------");
        bootstrap.bind(new InetSocketAddress(port)).sync();
        LOG.info("tcp server started on port：" + port);
        LOG.info("--------- start success! ------------");
    }

    public byte getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(byte protocolType) {
        this.protocolType = protocolType;
    }

    public PomeloSerializer getSerializer() {
        return serializer;
    }

    public void setSerializer(PomeloSerializer serializer) {
        this.serializer = serializer;
    }

    /**
     * @return the threadCount
     */
    public int getThreadCount() {
        return threadCount;
    }

    /**
     * @param threadCount the threadCount to set
     */
    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    private static class SingletonHolder {
        static final PomeloTcpServer instance = new PomeloTcpServer();
    }

}
