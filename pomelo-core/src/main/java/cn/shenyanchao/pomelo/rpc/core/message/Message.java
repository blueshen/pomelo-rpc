package cn.shenyanchao.pomelo.rpc.core.message;

import java.io.Serializable;

import cn.shenyanchao.pomelo.rpc.serialize.PomeloSerializer;

/**
 * 报文
 *
 * @author shenyanchao
 * @since 2019-03-06 21:46
 */
public abstract class Message implements Serializable {

    protected byte protocolType;
    protected PomeloSerializer serializer;
    protected int messageLen;
    protected int requestId;

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

    public int getMessageLen() {
        return messageLen;
    }

    public void setMessageLen(int messageLen) {
        this.messageLen = messageLen;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }
}
