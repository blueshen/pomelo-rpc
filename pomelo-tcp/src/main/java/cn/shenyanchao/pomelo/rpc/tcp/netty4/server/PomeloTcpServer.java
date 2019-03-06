package cn.shenyanchao.pomelo.rpc.tcp.netty4.server;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.shenyanchao.pomelo.rpc.core.server.RpcServer;
import cn.shenyanchao.pomelo.rpc.core.server.filter.RpcFilter;
import cn.shenyanchao.pomelo.rpc.core.server.handler.factory.PomeloRpcServerHandlerFactory;
import cn.shenyanchao.pomelo.rpc.core.thread.NamedThreadFactory;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.codec.PomeloRpcDecoderHandler;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.codec.PomeloRpcEncoderHandler;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.server.handler.PomeloRpcTcpServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
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

    private NioEventLoopGroup workerGroup;

    /**
     * 协议名称
     */
    private int protocolType;

    /**
     * 序列化类型
     */
    private int serializerType;

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
    public void registerService(String serviceName, Object serviceInstance, RpcFilter rpcFilter) {
        PomeloRpcServerHandlerFactory.getServerHandler().addHandler(serviceName, serviceInstance, rpcFilter);
    }

    @Override
    public void stop() {
        PomeloRpcServerHandlerFactory.getServerHandler().clear();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    @Override
    public void run(final int port, final int timeout) throws Exception {
        ThreadFactory serverBossTF = new NamedThreadFactory("pomelo-io-");
        ThreadFactory serverWorkerTF = new NamedThreadFactory("pomelo-worker-");
        bossGroup = new NioEventLoopGroup(PROCESSORS, serverBossTF);
        workerGroup = new NioEventLoopGroup(PROCESSORS * 2, serverWorkerTF);
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
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
                pipeline.addLast("handler", new PomeloRpcTcpServerHandler(threadCount, protocolType, serializerType));

            }

        });
        LOG.info("----------- pomelo starting ------------");
        bootstrap.bind(new InetSocketAddress(port)).sync();
        LOG.info("tcp server started on port：" + port);
        LOG.info("--------- start success! ------------");
    }

    public void setProtocolType(int protocolType) {
        this.protocolType = protocolType;
    }

    public int getSerializerType() {
        return serializerType;
    }

    public void setSerializerType(int serializerType) {
        this.serializerType = serializerType;
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
