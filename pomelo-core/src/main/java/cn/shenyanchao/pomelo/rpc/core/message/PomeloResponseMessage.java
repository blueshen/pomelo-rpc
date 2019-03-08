package cn.shenyanchao.pomelo.rpc.core.message;

import cn.shenyanchao.pomelo.rpc.serialize.PomeloSerializer;

/**
 * RPC返回实体
 *
 * @author shenyanchao
 */
public class PomeloResponseMessage extends Message {

    private static final long serialVersionUID = 4523576666635080090L;

    private Object response = null;

    private boolean isError = false;

    private Throwable exception = null;

    private byte[] responseClassName;

    public PomeloResponseMessage(int requestId, PomeloSerializer serializer, byte protocolType) {
        this.requestId = requestId;
        this.serializer = serializer;
        this.protocolType = protocolType;
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
