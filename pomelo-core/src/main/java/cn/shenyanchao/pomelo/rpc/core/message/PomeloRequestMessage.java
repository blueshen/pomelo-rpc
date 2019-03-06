package cn.shenyanchao.pomelo.rpc.core.message;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * RPC请求实体类
 *
 * @author shenyanchao
 */
public class PomeloRequestMessage implements Message {

    private static final long serialVersionUID = -3554311529871950375L;

    private static final AtomicInteger requestIdSeq = new AtomicInteger();
    private byte[] targetInstanceName;
    private byte[] methodName;
    private byte[][] argTypes;
    private Object[] requestObjects;
    private Object message = null;
    private int timeout;
    private int id;
    private int protocolType;
    private int serializerType;
    private int messageLen;

    public PomeloRequestMessage(byte[] targetInstanceName, byte[] methodName, byte[][] argTypes,
                                Object[] requestObjects, int timeout, int codecType, int protocolType) {
        this(targetInstanceName, methodName, argTypes, requestObjects, timeout, get(), codecType, protocolType);
    }

    public PomeloRequestMessage(byte[] targetInstanceName, byte[] methodName, byte[][] argTypes,
                                Object[] requestObjects, int timeout, int id, int codecType, int protocolType) {
        this.requestObjects = requestObjects;
        this.id = id;
        this.timeout = timeout;
        this.targetInstanceName = targetInstanceName;
        this.methodName = methodName;
        this.argTypes = argTypes;
        this.serializerType = codecType;
        this.protocolType = protocolType;

    }

    public static Integer get() {
        return requestIdSeq.incrementAndGet();
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

    public Object getMessage() {
        return message;
    }

    public byte[] getTargetInstanceName() {
        return targetInstanceName;
    }

    public byte[] getMethodName() {
        return methodName;
    }

    public int getTimeout() {
        return timeout;
    }

    public Object[] getRequestObjects() {
        return requestObjects;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    public byte[][] getArgTypes() {
        return argTypes;
    }

    public void setArgTypes(byte[][] argTypes) {
        this.argTypes = argTypes;
    }
}
