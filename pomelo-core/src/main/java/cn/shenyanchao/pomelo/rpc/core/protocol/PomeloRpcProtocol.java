package cn.shenyanchao.pomelo.rpc.core.protocol;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cn.shenyanchao.pomelo.rpc.core.bytebuffer.RpcByteBuffer;
import cn.shenyanchao.pomelo.rpc.core.message.Message;

/**
 * RPC协议, 编码/解码
 *
 * @author shenyanchao
 */

@Singleton
public class PomeloRpcProtocol {

    public static final int HEADER_LEN = 2;

    public static final byte CURRENT_VERSION = (byte) 1;

    @Inject
    private DefaultRpcProtocol defaultRpcProtocol;

    public RpcByteBuffer encode(Message message, RpcByteBuffer byteBufferWrapper) throws Exception {
        //        byte type = 0;
        //        if (message instanceof PomeloRequestMessage) {
        //            type = ((PomeloRequestMessage) message).getProtocolType();
        //        } else if (message instanceof PomeloResponseMessage) {
        //            type = ((PomeloResponseMessage) message).getProtocolType();
        //        }
        // TODO 依据不同的protocol，使用不同的协议
        return defaultRpcProtocol.encode(message, byteBufferWrapper);
    }

    public Message decode(RpcByteBuffer wrapper, Message errorObject) throws Exception {
        final int originPos = wrapper.readerIndex();
        if (wrapper.readableBytes() < 2) {
            wrapper.setReaderIndex(originPos);
            return errorObject;
        }
        byte version = wrapper.readByte();
        if (version == CURRENT_VERSION) {
            // 此处是有意义的,把header读取了.
            byte protocolType = wrapper.readByte();
            //            RpcProtocol protocol = DefaultRpcProtocol.getInstance();
            //            if (protocol == null) {
            //                throw new Exception("UnSupport protocol type: " + type);
            //            }
            return defaultRpcProtocol.decode(wrapper, errorObject, new int[] {originPos});
        } else {
            throw new Exception("UnSupport protocol version: " + version);
        }
    }

}
