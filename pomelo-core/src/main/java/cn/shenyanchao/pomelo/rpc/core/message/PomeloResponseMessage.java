package cn.shenyanchao.pomelo.rpc.core.message;

/**
 * RPC返回实体
 *
 * @author shenyanchao
 */
public class PomeloResponseMessage implements Message {

    private static final long serialVersionUID = 4523576666635080090L;

    private int requestId;

    private Object response = null;

    private boolean isError = false;

    private Throwable exception = null;

    private int serializerType;

    private int protocolType;

    private int messageLen;

    private byte[] responseClassName;

    public PomeloResponseMessage(int requestId, int codecType, int protocolType) {
        this.requestId = requestId;
        this.serializerType = codecType;
        this.protocolType = protocolType;
    }

    public int getMessageLen() {
        return messageLen;
    }

    public void setMessageLen(int messageLen) {
        this.messageLen = messageLen;
    }

    public int getProtocolType() {
        return protocolType;
    }

    public int getSerializerType() {
        return serializerType;
    }

    public int getRequestId() {
        return requestId;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public boolean isError() {
        return isError;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
        isError = true;
    }

    public byte[] getResponseClassName() {
        return responseClassName;
    }

    public void setResponseClassName(byte[] responseClassName) {
        this.responseClassName = responseClassName;
    }
}
