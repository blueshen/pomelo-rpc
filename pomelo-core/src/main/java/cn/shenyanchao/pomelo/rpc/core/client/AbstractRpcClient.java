package cn.shenyanchao.pomelo.rpc.core.client;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.shenyanchao.pomelo.rpc.core.serialize.PomeloRpcSerializers;
import cn.shenyanchao.pomelo.rpc.core.message.PomeloRequestMessage;
import cn.shenyanchao.pomelo.rpc.core.message.PomeloResponseMessage;
import cn.shenyanchao.pomelo.rpc.core.util.StringUtil;

/**
 * @author shenyanchao
 */
public abstract class AbstractRpcClient implements RpcClient {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractRpcClient.class);

    @Override
    public Object invokeImpl(String targetInstanceName, String methodName,
                             String[] argTypes, Object[] args, int timeout, int codecType,
                             int protocolType) throws Exception {
        byte[][] argTypeBytes = new byte[argTypes.length][];
        for (int i = 0; i < argTypes.length; i++) {
            argTypeBytes[i] = argTypes[i].getBytes();
        }

        PomeloRequestMessage wrapper = new PomeloRequestMessage(targetInstanceName.getBytes(),
                methodName.getBytes(), argTypeBytes, args, timeout, codecType, protocolType);

        return invokeImplIntern(wrapper);
    }

    private Object invokeImplIntern(PomeloRequestMessage requestMessage) throws Exception {
        long beginTime = System.currentTimeMillis();
        LinkedBlockingQueue<Object> responseQueue = new LinkedBlockingQueue<Object>(1);
        getRpcClientFactory().putResponse(requestMessage.getId(), responseQueue);
        PomeloResponseMessage rpcResponse = null;

        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("client ready to send message,request id:{} ", requestMessage.getId());
            }

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

            result = responseQueue.poll(
                    requestMessage.getTimeout() - (System.currentTimeMillis() - beginTime),
                    TimeUnit.MILLISECONDS);
        } finally {
            getRpcClientFactory().removeResponse(requestMessage.getId());
        }
        if (null == result && (System.currentTimeMillis() - beginTime) <= requestMessage.getTimeout()) {
            //返回结果集为null
            rpcResponse = new PomeloResponseMessage(requestMessage.getId(), requestMessage.getSerializerType(),
                    requestMessage.getProtocolType());
        } else if (null == result && (System.currentTimeMillis() - beginTime) > requestMessage.getTimeout()) {
            //结果集超时
            StringBuffer errorMsg = new StringBuffer();
            errorMsg.append("receive response timeout(").append(requestMessage.getTimeout()).append(" ms),server is: ")
                    .append(getServerIP()).append(":").append(getServerPort()).append(" request id is:")
                    .append(requestMessage.getId());

            LOG.error(errorMsg.toString());
            rpcResponse = new PomeloResponseMessage(requestMessage.getId(), requestMessage.getSerializerType(),
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
                    Object responseObject = PomeloRpcSerializers.getSerialization(rpcResponse.getSerializerType()).deserialize(
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

        if (!StringUtil.isBlank(rpcResponse.getException())) {
            Throwable t = rpcResponse.getException();
            LOG.error("server error,server is:{}:{}, request id is:{}", getServerIP(), getServerPort(),
                    requestMessage.getId(), t);
            return null;
        }

        return rpcResponse.getResponse();
    }

    /**
     * 发送请求
     *
     * @throws Exception
     */
    public abstract void sendMessage(PomeloRequestMessage commonRpcRequest) throws Exception;

    /**
     * 消灭消息
     *
     * @throws Exception
     */
    public abstract void destroy() throws Exception;
}
