package cn.shenyanchao.pomelo.rpc.tcp.netty4.client;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.shenyanchao.pomelo.rpc.core.client.AbstractRpcClient;
import cn.shenyanchao.pomelo.rpc.core.client.factory.RpcClientFactory;
import cn.shenyanchao.pomelo.rpc.core.message.PomeloRequestMessage;
import cn.shenyanchao.pomelo.rpc.core.message.PomeloResponseMessage;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.client.factory.PomeloRpcTcpClientFactory;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * @author shenyanchao
 */
public class PomeloRpcClient extends AbstractRpcClient {

    private static final Logger LOG = LoggerFactory.getLogger(PomeloRpcClient.class);

    private ChannelFuture cf;

    public PomeloRpcClient(ChannelFuture cf) {
        this.cf = cf;
    }

    @Override
    public String getServerIP() {
        return ((InetSocketAddress) cf.channel().remoteAddress()).getHostName();
    }

    @Override
    public int getServerPort() {
        return ((InetSocketAddress) cf.channel().remoteAddress()).getPort();
    }

    @Override
    public RpcClientFactory getRpcClientFactory() {
        return PomeloRpcTcpClientFactory.getInstance();
    }

    @Override
    public void sendMessage(final PomeloRequestMessage requestMessage) throws Exception {

        if (cf.channel().isOpen()) {
            ChannelFuture writeFuture = cf.channel().writeAndFlush(requestMessage);
            // use listener to avoid wait for write & thread context switch
            writeFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future)
                        throws Exception {
                    if (future.isSuccess()) {
                        return;
                    }
                    StringBuffer errorMsg = new StringBuffer();
                    // write timeout
                    if (future.isCancelled()) {
                        errorMsg.append("Send request to ").append(cf.channel().toString()).append(" cancelled by "
                                + "user ,request id is ").append(requestMessage.getId());
                    } else if (!future.isSuccess()) {
                        if (cf.channel().isOpen()) {
                            // maybe some exception,so close the channel
                            cf.channel().close();
                            getRpcClientFactory().removeRpcClient(getServerIP(), getServerPort());
                        }
                        errorMsg.append("Send request to ").append(cf.channel().toString()).append(" error.")
                                .append(future.cause().toString());
                    }
                    LOG.error(errorMsg.toString());
                    PomeloResponseMessage response =
                            new PomeloResponseMessage(requestMessage.getId(), requestMessage.getSerializerType(),
                                    requestMessage.getProtocolType());
                    response.setException(new Exception(errorMsg.toString()));
                    getRpcClientFactory().receiveResponse(response);
                }
            });
        }

    }

    @Override
    public void destroy() {
        getRpcClientFactory().removeRpcClient(getServerIP(), getServerPort());
        if (cf.channel().isOpen()) {
            cf.channel().close();
        }
    }

}
