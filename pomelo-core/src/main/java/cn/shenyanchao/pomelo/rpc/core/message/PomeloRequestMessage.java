package cn.shenyanchao.pomelo.rpc.core.message;

import java.util.concurrent.atomic.AtomicInteger;

import cn.shenyanchao.pomelo.rpc.serialize.PomeloSerializer;

/**
 * RPC请求实体类
 *
 * @author shenyanchao
 */
public class PomeloRequestMessage extends Message {

    private static final long serialVersionUID = -3554311529871950375L;

    private static final AtomicInteger requestIdSeq = new AtomicInteger();
    private byte[] targetInstanceName;
    private byte[] methodName;
    private byte[][] argTypes;
    private Object[] requestObjects;
    private Object message = null;
    private int timeout;
    private int id;

    public PomeloRequestMessage(byte[] targetInstanceName, byte[] methodName, byte[][] argTypes,
                                Object[] requestObjects, int timeout, PomeloSerializer serializer, byte protocolType) {
        this(targetInstanceName, methodName, argTypes, requestObjects, timeout, get(), serializer, protocolType);
    }

    public PomeloRequestMessage(byte[] targetInstanceName, byte[] methodName, byte[][] argTypes,
                                Object[] requestObjects, int timeout, int id, PomeloSerializer serializer, byte protocolType) {
        this.requestObjects = requestObjects;
        this.id = id;
        this.timeout = timeout;
        this.targetInstanceName = targetInstanceName;
        this.methodName = methodName;
        this.argTypes = argTypes;
        this.serializer = serializer;
        this.protocolType = protocolType;

    }

    public static Integer get() {
        return requestIdSeq.incrementAndGet();
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
