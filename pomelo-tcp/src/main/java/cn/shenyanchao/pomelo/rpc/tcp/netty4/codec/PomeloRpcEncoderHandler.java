package cn.shenyanchao.pomelo.rpc.tcp.netty4.codec;

import cn.shenyanchao.pomelo.rpc.core.message.Message;
import cn.shenyanchao.pomelo.rpc.core.protocol.PomeloRpcProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author shenyanchao
 */
public class PomeloRpcEncoderHandler extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, ByteBuf out)
            throws Exception {
        PomeloRpcByteBuffer byteBufferWrapper = new PomeloRpcByteBuffer(ctx);
        PomeloRpcProtocol.encode(message, byteBufferWrapper);
        ctx.write(byteBufferWrapper.getBuffer());
    }

}
