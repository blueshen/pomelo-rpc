package cn.shenyanchao.pomelo.rpc.tcp.netty4.server.handler;

import cn.shenyanchao.pomelo.rpc.core.message.Message;
import cn.shenyanchao.pomelo.rpc.core.protocol.PomeloRpcProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author shenyanchao
 */

public class PomeloRpcEncoderHandler extends MessageToByteEncoder<Message> {

    private PomeloRpcProtocol pomeloRpcProtocol;

    public PomeloRpcEncoderHandler(PomeloRpcProtocol pomeloRpcProtocol) {
        super();
        this.pomeloRpcProtocol = pomeloRpcProtocol;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, ByteBuf out)
            throws Exception {
        PomeloRpcByteBuffer byteBufferWrapper = new PomeloRpcByteBuffer(ctx);
        pomeloRpcProtocol.encode(message, byteBufferWrapper);
        ctx.write(byteBufferWrapper.getBuffer());
    }

}
