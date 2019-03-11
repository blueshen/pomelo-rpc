package cn.shenyanchao.pomelo.rpc.tcp.netty4.client;

import java.net.InetSocketAddress;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.shenyanchao.pomelo.rpc.core.message.PomeloRequestMessage;
import cn.shenyanchao.pomelo.rpc.core.message.PomeloResponseMessage;
import cn.shenyanchao.pomelo.rpc.serialize.PomeloSerializer;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * @author shenyanchao
 */
public class PomeloRpcClient implements RpcClient {

    private static final Logger LOG = LoggerFactory.getLogger(PomeloRpcClient.class);

    private ChannelFuture cf;
    private ClientHolder clientHolder;
    private ResponseHolder responseModule;

    public PomeloRpcClient(ChannelFuture cf, ResponseHolder responseModule, ClientHolder clientHolder) {
        this.cf = cf;
        this.responseModule = responseModule;
        this.clientHolder = clientHolder;
    }

    @Override
    public Object invokeImpl(String targetInstanceName, String methodName,
                             String[] argTypes, Object[] args, int timeout, PomeloSerializer serializer,
                             byte protocolType) throws Exception {
        byte[][] argTypeBytes = new byte[argTypes.length][];
        for (int i = 0; i < argTypes.length; i++) {
            argTypeBytes[i] = argTypes[i].getBytes();
        }

        PomeloRequestMessage wrapper = new PomeloRequestMessage(targetInstanceName.getBytes(),
                methodName.getBytes(), argTypeBytes, args, timeout, serializer, protocolType);

        return invokeImplIntern(wrapper);
    }

    private Object invokeImplIntern(PomeloRequestMessage requestMessage) throws Exception {
        long beginTime = System.currentTimeMillis();
        LinkedBlockingQueue<Object> responseQueue = new LinkedBlockingQueue<>(1);
        responseModule.putResponse(requestMessage.getId(), responseQueue);
        PomeloResponseMessage rpcResponse = null;

        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("client ready to send message,request id:{} ", requestMessage.getId());
            }
            // fire
            sendMessage(requestMessage);

            if (LOG.isDebugEnabled()) {
                LOG.debug("client write message to send buffer, wait for response,request id: {}",
                        requestMessage.getId());
            }
        } catch (Exception e) {
            LOG.error("send request to os sendbuffer error", e);
            throw new RuntimeException("send request to os sendbuffer error", e);
        }
        Object result;
        try {
            long leftTime = requestMessage.getTimeout() - (System.currentTimeMillis() - beginTime);
            if (LOG.isDebugEnabled()) {
                LOG.debug("poll from responseQueue timeout={}ms", leftTime);
            }
            result = responseQueue.poll(
                    requestMessage.getTimeout() - (System.currentTimeMillis() - beginTime),
                    TimeUnit.MILLISECONDS);
        } finally {
            responseModule.removeResponse(requestMessage.getId());
        }
        if (null == result && (System.currentTimeMillis() - beginTime) <= requestMessage.getTimeout()) {
            //返回结果集为null
            rpcResponse = new PomeloResponseMessage(requestMessage.getId(), requestMessage.getSerializer(),
                    requestMessage.getProtocolType());
        } else if (null == result && (System.currentTimeMillis() - beginTime) > requestMessage.getTimeout()) {
            //结果集超时
            StringBuffer errorMsg = new StringBuffer();
            errorMsg.append("receive response timeout(").append(requestMessage.getTimeout()).append(" ms),server is: ")
                    .append(getServerIP()).append(":").append(getServerPort()).append(" request id is:")
                    .append(requestMessage.getId());

            LOG.error(errorMsg.toString());
            rpcResponse = new PomeloResponseMessage(requestMessage.getId(), requestMessage.getSerializer(),
                    requestMessage.getProtocolType());
            rpcResponse.setException(new Throwable(errorMsg.toString()));
        } else if (result != null) {
            rpcResponse = (PomeloResponseMessage) result;
        }

        try {
            if (rpcResponse.getResponse() instanceof byte[]) {
                String responseClassName = null;
                if (rpcResponse.getResponseClassName() != null) {
                    responseClassName = new String(rpcResponse.getResponseClassName());
                }
                if (((byte[]) rpcResponse.getResponse()).length == 0) {
                    rpcResponse.setResponse(null);
                } else {
                    Object responseObject = rpcResponse.getSerializer().getSerialization().deserialize(
                            responseClassName, (byte[]) rpcResponse.getResponse());
                    if (responseObject instanceof Throwable) {
                        rpcResponse.setException((Throwable) responseObject);
                    } else {
                        rpcResponse.setResponse(responseObject);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Deserialize response object error", e);
            throw new Exception("Deserialize response object error", e);
        }

        if (null != rpcResponse.getException()) {
            Throwable t = rpcResponse.getException();
            LOG.error("server error,server is:{}:{}, request id is:{}", getServerIP(), getServerPort(),
                    requestMessage.getId(), t);
            return null;
        }

        return rpcResponse.getResponse();
    }

    @Override
    public String getServerIP() {
        return ((InetSocketAddress) cf.channel().remoteAddress()).getHostName();
    }

    @Override
    public int getServerPort() {
        return ((InetSocketAddress) cf.channel().remoteAddress()).getPort();
    }

    public void sendMessage(final PomeloRequestMessage requestMessage) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("------------> send message to {}<-----------", cf.channel().toString());
        }

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

    public void destroy() {
        clientHolder.removeRpcClient(getServerIP(), getServerPort());
        if (cf.channel().isOpen()) {
            cf.channel().close();
        }
    }

}
