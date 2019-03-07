package cn.shenyanchao.pomelo.rpc.core.protocol;

import cn.shenyanchao.pomelo.rpc.core.bytebuffer.RpcByteBuffer;
import cn.shenyanchao.pomelo.rpc.core.message.Message;
import cn.shenyanchao.pomelo.rpc.core.message.PomeloRequestMessage;
import cn.shenyanchao.pomelo.rpc.core.message.PomeloResponseMessage;

/**
 * RPC协议, 编码/解码
 *
 * @author shenyanchao
 */
public class PomeloRpcProtocol {

    public static final int HEADER_LEN = 2;

    public static final byte CURRENT_VERSION = (byte) 1;

    public static RpcByteBuffer encode(Message message, RpcByteBuffer byteBufferWrapper) throws Exception {
        Integer type = 0;
        if (message instanceof PomeloRequestMessage) {
            type = ((PomeloRequestMessage) message).getProtocolType();
        } else if (message instanceof PomeloResponseMessage) {
            type = ((PomeloResponseMessage) message).getProtocolType();
        }
        return DefaultRpcProtocol.getInstance().encode(message, byteBufferWrapper);
    }

    public static Message decode(RpcByteBuffer wrapper, Message errorObject) throws Exception {
        final int originPos = wrapper.readerIndex();
        if (wrapper.readableBytes() < 2) {
            wrapper.setReaderIndex(originPos);
            return errorObject;
        }
        int version = wrapper.readByte();
        if (version == 1) {
            int type = wrapper.readByte();
            RpcProtocol protocol = DefaultRpcProtocol.getInstance();
            if (protocol == null) {
                throw new Exception("UnSupport protocol type: " + type);
            }
            return protocol.decode(wrapper, errorObject, new int[] {originPos});
        } else {
            throw new Exception("UnSupport protocol version: " + version);
        }
    }

}
