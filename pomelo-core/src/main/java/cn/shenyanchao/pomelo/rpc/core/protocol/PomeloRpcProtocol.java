package cn.shenyanchao.pomelo.rpc.core.protocol;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cn.shenyanchao.pomelo.rpc.core.bytebuffer.RpcByteBuffer;
import cn.shenyanchao.pomelo.rpc.core.message.Message;
import cn.shenyanchao.pomelo.rpc.core.message.PomeloRequestMessage;
import cn.shenyanchao.pomelo.rpc.core.message.PomeloResponseMessage;

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

    public  RpcByteBuffer encode(Message message, RpcByteBuffer byteBufferWrapper) throws Exception {
        return defaultRpcProtocol.encode(message, byteBufferWrapper);
    }

    public  Message decode(RpcByteBuffer wrapper, Message errorObject) throws Exception {
        final int originPos = wrapper.readerIndex();
        if (wrapper.readableBytes() < 2) {
            wrapper.setReaderIndex(originPos);
            return errorObject;
        }
//        byte version = wrapper.readByte();
//        if (version == 1) {
            return defaultRpcProtocol.decode(wrapper, errorObject, new int[] {originPos});
//        } else {
//            throw new Exception("UnSupport protocol version: " + version);
//        }
    }

}
