package cn.shenyanchao.pomelo.rpc.tcp.netty4.client;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.shenyanchao.pomelo.rpc.core.message.PomeloRequestMessage;
import cn.shenyanchao.pomelo.rpc.core.message.PomeloResponseMessage;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * @author shenyanchao
 */
public class PomeloRpcClient extends AbstractRpcClient {

    private static final Logger LOG = LoggerFactory.getLogger(PomeloRpcClient.class);

    private ChannelFuture cf;

    private ClientHolder clientHolder;

    public PomeloRpcClient(ChannelFuture cf, ResponseModule responseModule, ClientHolder clientHolder) {
        this.cf = cf;
        this.responseModule = responseModule;
        this.clientHolder = clientHolder;
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
    public void sendMessage(final PomeloRequestMessage requestMessage) {
        LOG.debug("------------{}-------", cf.channel().toString());

        if (cf.channel().isOpen()) {
            ChannelFuture writeFuture = cf.channel().writeAndFlush(requestMessage);
            // use listener to avoid wait for write & thread context switch
            writeFuture.addListener((ChannelFutureListener) future -> {
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
                        clientHolder.removeRpcClient(getServerIP(), getServerPort());
                    }
                    errorMsg.append("Send request to ").append(cf.channel().toString()).append(" error.")
                            .append(future.cause().toString());
                }
                LOG.error(errorMsg.toString());
                PomeloResponseMessage response =
                        new PomeloResponseMessage(requestMessage.getId(), requestMessage.getSerializer(),
                                requestMessage.getProtocolType());
                response.setException(new Exception(errorMsg.toString()));
                responseModule.receiveResponse(response);
            });
        }

    }

    @Override
    public void destroy() {

        clientHolder.removeRpcClient(getServerIP(), getServerPort());
        if (cf.channel().isOpen()) {
            cf.channel().close();
        }
    }

}
