package cn.shenyanchao.pomelo.rpc.tcp.netty4.codec;

import java.util.List;

import cn.shenyanchao.pomelo.rpc.core.protocol.PomeloRpcProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * @author shenyanchao
 */
public class PomeloRpcDecoderHandler extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf,
                          List<Object> out) throws Exception {
        PomeloRpcByteBuffer wrapper = new PomeloRpcByteBuffer(buf);
        Object result = PomeloRpcProtocol.decode(wrapper, null);

        if (result != null) {
            out.add(result);
        }
    }

}
