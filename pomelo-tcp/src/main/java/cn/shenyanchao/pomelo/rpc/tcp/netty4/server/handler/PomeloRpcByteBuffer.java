package cn.shenyanchao.pomelo.rpc.tcp.netty4.server.handler;

import cn.shenyanchao.pomelo.rpc.core.bytebuffer.RpcByteBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author shenyanchao
 */
public class PomeloRpcByteBuffer implements RpcByteBuffer {

    private ByteBuf buffer;

    private ChannelHandlerContext ctx;

    public PomeloRpcByteBuffer(ByteBuf in) {
        buffer = in;
    }

    public PomeloRpcByteBuffer(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public PomeloRpcByteBuffer get(int capacity) {
        if (buffer != null) {
            return this;
        }
        buffer = ctx.alloc().buffer(capacity);
        return this;
    }

    @Override
    public byte readByte() {
        return buffer.readByte();
    }

    @Override
    public void readBytes(byte[] dst) {
        buffer.readBytes(dst);
    }

    @Override
    public int readInt() {
        return buffer.readInt();
    }

    @Override
    public int readableBytes() {
        return buffer.readableBytes();
    }

    @Override
    public int readerIndex() {
        return buffer.readerIndex();
    }

    @Override
    public void setReaderIndex(int index) {
        buffer.setIndex(index, buffer.writerIndex());
    }

    @Override
    public void writeByte(byte data) {
        buffer.writeByte(data);
    }

    @Override
    public void writeBytes(byte[] data) {
        buffer.writeBytes(data);
    }

    @Override
    public void writeInt(int data) {
        buffer.writeInt(data);
    }

    public ByteBuf getBuffer() {
        return buffer;
    }

    @Override
    public void writeByte(int index, byte data) {
        buffer.writeByte(data);
    }
}
