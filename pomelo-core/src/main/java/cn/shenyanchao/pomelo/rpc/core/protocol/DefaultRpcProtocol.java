package cn.shenyanchao.pomelo.rpc.core.protocol;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.shenyanchao.pomelo.rpc.core.bytebuffer.RpcByteBuffer;
import cn.shenyanchao.pomelo.rpc.core.message.Message;
import cn.shenyanchao.pomelo.rpc.core.serialize.PomeloRpcSerializers;
import cn.shenyanchao.pomelo.rpc.core.message.PomeloRequestMessage;
import cn.shenyanchao.pomelo.rpc.core.message.PomeloResponseMessage;

/**
 * 协议实现,每个字节代表什么
 */
public class DefaultRpcProtocol implements RpcProtocol {

    public static final int TYPE = 1;

    private static final Logger LOG = LoggerFactory.getLogger(DefaultRpcProtocol.class);

    private static final int REQUEST_HEADER_LEN = 1 * 6 + 5 * 4;

    private static final int RESPONSE_HEADER_LEN = 1 * 6 + 3 * 4;

    private static final byte VERSION = (byte) 1;

    private static final byte REQUEST = (byte) 0;

    private static final byte RESPONSE = (byte) 1;

    private DefaultRpcProtocol(){

    }

    public static RpcProtocol getInstance() {
        return SingletonHolder.rpcProtocol;
    }

    @Override
    public RpcByteBuffer encode(Message message,
                                RpcByteBuffer byteBufferWrapper) throws Exception {
        if (!(message instanceof PomeloRequestMessage)
                && !(message instanceof PomeloResponseMessage)) {
            throw new Exception(
                    "unknown Message");
        }
        int id = 0;
        byte type = REQUEST;
        if (message instanceof PomeloRequestMessage) {
            try {
                int requestArgTypesLen = 0;
                int requestArgsLen = 0;
                List<byte[]> requestArgTypes = new ArrayList<>();
                List<byte[]> requestArgs = new ArrayList<>();
                PomeloRequestMessage wrapper = (PomeloRequestMessage) message;
                byte[][] requestArgTypeStrings = wrapper.getArgTypes();
                for (byte[] requestArgType : requestArgTypeStrings) {
                    requestArgTypes.add(requestArgType);
                    requestArgTypesLen += requestArgType.length;
                }
                Object[] requestObjects = wrapper.getRequestObjects();
                if (requestObjects != null) {
                    for (Object requestArg : requestObjects) {
                        byte[] requestArgByte = PomeloRpcSerializers.getSerialization(
                                wrapper.getSerializerType()).serialize(requestArg);
                        requestArgs.add(requestArgByte);
                        requestArgsLen += requestArgByte.length;
                    }
                }
                byte[] targetInstanceNameByte = wrapper.getTargetInstanceName();
                byte[] methodNameByte = wrapper.getMethodName();

                id = wrapper.getId();
                int timeout = wrapper.getTimeout();
                int capacity = PomeloRpcProtocol.HEADER_LEN + REQUEST_HEADER_LEN
                        + requestArgs.size() * 4 * 2
                        + targetInstanceNameByte.length + methodNameByte.length
                        + requestArgTypesLen + requestArgsLen;

                RpcByteBuffer byteBuffer = byteBufferWrapper.get(capacity);
                byteBuffer.writeByte(PomeloRpcProtocol.CURRENT_VERSION);
                byteBuffer.writeByte((byte) TYPE);
                //--------------HEADER_LEN----------------
                byteBuffer.writeByte(VERSION);//1B
                byteBuffer.writeByte(type);//1B
                byteBuffer.writeByte((byte) wrapper.getSerializerType());//1B
                byteBuffer.writeByte((byte) 0);//1B
                byteBuffer.writeByte((byte) 0);//1B
                byteBuffer.writeByte((byte) 0);//1B
                byteBuffer.writeInt(id); //4B
                byteBuffer.writeInt(timeout);//4B
                byteBuffer.writeInt(targetInstanceNameByte.length);//4B

                byteBuffer.writeInt(methodNameByte.length);//4B

                byteBuffer.writeInt(requestArgs.size());//4B
                //---------------REQUEST_HEADER_LEN----------

                for (byte[] requestArgType : requestArgTypes) {
                    byteBuffer.writeInt(requestArgType.length);
                }
                for (byte[] requestArg : requestArgs) {
                    byteBuffer.writeInt(requestArg.length);
                }
                byteBuffer.writeBytes(targetInstanceNameByte);

                byteBuffer.writeBytes(methodNameByte);

                for (byte[] requestArgType : requestArgTypes) {
                    byteBuffer.writeBytes(requestArgType);
                }
                for (byte[] requestArg : requestArgs) {
                    byteBuffer.writeBytes(requestArg);
                }
                return byteBuffer;
            } catch (Exception e) {
                LOG.error("encode request object error", e);
                throw e;
            }
        } else {
            PomeloResponseMessage wrapper = (PomeloResponseMessage) message;
            byte[] body = new byte[0];
            byte[] className = new byte[0];
            try {
                // no return object
                if (wrapper.getResponse() != null) {
                    className = wrapper.getResponse().getClass().getName()
                            .getBytes();
                    body = PomeloRpcSerializers.getSerialization(wrapper.getSerializerType())
                            .serialize(wrapper.getResponse());
                }
                if (wrapper.isError()) {
                    className = wrapper.getException().getClass().getName()
                            .getBytes();
                    body = PomeloRpcSerializers.getSerialization(wrapper.getSerializerType())
                            .serialize(wrapper.getException());
                }
                id = wrapper.getRequestId();
            } catch (Exception e) {
                LOG.error("serialize response object error", e);
                // still create responsewrapper,so client can get exception
                wrapper.setResponse(new Exception(
                        "serialize response object error", e));
                className = Exception.class.getName().getBytes();
                body = PomeloRpcSerializers.getSerialization(wrapper.getSerializerType())
                        .serialize(wrapper.getResponse());
            }
            type = RESPONSE;
            int capacity = PomeloRpcProtocol.HEADER_LEN + RESPONSE_HEADER_LEN
                    + body.length;
            if (wrapper.getSerializerType() == PomeloRpcSerializers.PB_CODEC) {
                capacity += className.length;
            }
            RpcByteBuffer byteBuffer = byteBufferWrapper.get(capacity);
            byteBuffer.writeByte(PomeloRpcProtocol.CURRENT_VERSION); // 1B
            byteBuffer.writeByte((byte) TYPE);//1B
            //--------------HEADER_LEN----------------
            byteBuffer.writeByte(VERSION);//1B
            byteBuffer.writeByte(type);  //1B
            byteBuffer.writeByte((byte) wrapper.getSerializerType());//1B
            byteBuffer.writeByte((byte) 0);//1B
            byteBuffer.writeByte((byte) 0);//1B
            byteBuffer.writeByte((byte) 0);//1B
            //---------------REQUEST_HEADER_LEN----------
            byteBuffer.writeInt(id);//4B
            if (wrapper.getSerializerType() == PomeloRpcSerializers.PB_CODEC) {
                byteBuffer.writeInt(className.length);
            } else {
                byteBuffer.writeInt(0);//4B
            }
            byteBuffer.writeInt(body.length);//4B
            if (wrapper.getSerializerType() == PomeloRpcSerializers.PB_CODEC) {
                byteBuffer.writeBytes(className);
            }
            byteBuffer.writeBytes(body);
            return byteBuffer;
        }
    }

    @Override
    public Message decode(RpcByteBuffer wrapper, Message errorObject,
                         int... originPosArray) throws Exception {
        final int originPos;
        if (originPosArray != null && originPosArray.length == 1) {
            originPos = originPosArray[0];
        } else {
            originPos = wrapper.readerIndex();
        }
        if (wrapper.readableBytes() < 2) {
            wrapper.setReaderIndex(originPos);
            return errorObject;
        }
        byte version = wrapper.readByte();
        if (version == (byte) TYPE) {
            byte type = wrapper.readByte();
            if (type == REQUEST) {
                if (wrapper.readableBytes() < REQUEST_HEADER_LEN - 2) {
                    wrapper.setReaderIndex(originPos);
                    return errorObject;
                }
                int codecType = wrapper.readByte();
                wrapper.readByte();
                wrapper.readByte();
                wrapper.readByte();

                int requestId = wrapper.readInt();

                int timeout = wrapper.readInt();
                int targetInstanceLen = wrapper.readInt();

                int methodNameLen = wrapper.readInt();
                int argsCount = wrapper.readInt();
                int argInfosLen = argsCount * 4 * 2;
                int expectedLenInfoLen = argInfosLen + targetInstanceLen
                        + methodNameLen;
                if (wrapper.readableBytes() < expectedLenInfoLen) {
                    wrapper.setReaderIndex(originPos);
                    return errorObject;
                }
                int expectedLen = 0;
                int[] argsTypeLen = new int[argsCount];
                for (int i = 0; i < argsCount; i++) {
                    argsTypeLen[i] = wrapper.readInt();
                    expectedLen += argsTypeLen[i];
                }
                int[] argsLen = new int[argsCount];
                for (int i = 0; i < argsCount; i++) {
                    argsLen[i] = wrapper.readInt();
                    expectedLen += argsLen[i];
                }
                byte[] targetInstanceByte = new byte[targetInstanceLen];
                wrapper.readBytes(targetInstanceByte);

                byte[] methodNameByte = new byte[methodNameLen];
                wrapper.readBytes(methodNameByte);

                if (wrapper.readableBytes() < expectedLen) {
                    wrapper.setReaderIndex(originPos);
                    return errorObject;
                }
                byte[][] argTypes = new byte[argsCount][];
                for (int i = 0; i < argsCount; i++) {
                    byte[] argTypeByte = new byte[argsTypeLen[i]];
                    wrapper.readBytes(argTypeByte);
                    argTypes[i] = argTypeByte;
                }
                Object[] args = new Object[argsCount];
                for (int i = 0; i < argsCount; i++) {
                    byte[] argByte = new byte[argsLen[i]];
                    wrapper.readBytes(argByte);
                    args[i] = argByte;
                }

                PomeloRequestMessage pomeloRequestMessage = new PomeloRequestMessage(
                        targetInstanceByte, methodNameByte, argTypes, args,
                        timeout, requestId, codecType, TYPE);

                int messageLen = PomeloRpcProtocol.HEADER_LEN + REQUEST_HEADER_LEN
                        + expectedLenInfoLen + expectedLen;
                pomeloRequestMessage.setMessageLen(messageLen);
                return pomeloRequestMessage;

            } else if (type == RESPONSE) {
                if (wrapper.readableBytes() < RESPONSE_HEADER_LEN - 2) {
                    wrapper.setReaderIndex(originPos);
                    return errorObject;
                }
                int serializerType = wrapper.readByte();
                wrapper.readByte();
                wrapper.readByte();
                wrapper.readByte();

                int requestId = wrapper.readInt();

                int classNameLen = wrapper.readInt();
                int bodyLen = wrapper.readInt();
                if (wrapper.readableBytes() < classNameLen + bodyLen) {
                    wrapper.setReaderIndex(originPos);
                    return errorObject;
                }

                byte[] classNameBytes = null;
                if (serializerType == PomeloRpcSerializers.PB_CODEC) {
                    classNameBytes = new byte[classNameLen];
                    wrapper.readBytes(classNameBytes);
                }
                byte[] bodyBytes = new byte[bodyLen];
                wrapper.readBytes(bodyBytes);

                PomeloResponseMessage responseWrapper = new PomeloResponseMessage(
                        requestId, serializerType, TYPE);
                responseWrapper.setResponse(bodyBytes);
                responseWrapper.setResponseClassName(classNameBytes);
                int messageLen = PomeloRpcProtocol.HEADER_LEN + RESPONSE_HEADER_LEN
                        + classNameLen + bodyLen;
                responseWrapper.setMessageLen(messageLen);
                return responseWrapper;
            } else {
                throw new UnsupportedOperationException("protocol type : "
                        + type + " is not supported!");
            }
        } else {
            throw new UnsupportedOperationException("protocol version :"
                    + version + " is not supported!");
        }
    }

    public static class SingletonHolder {
        static DefaultRpcProtocol rpcProtocol = new DefaultRpcProtocol();
    }
}
