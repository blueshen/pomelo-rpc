package cn.shenyanchao.pomelo.rpc.core.bytebuffer;

/**
 * @author shenyanchao
 */
public interface RpcByteBuffer {

    RpcByteBuffer get(int capacity);

    void writeByte(int index, byte data);

    void writeByte(byte data);

    byte readByte();

    void writeInt(int data);

    void writeBytes(byte[] data);

    int readableBytes();

    int readInt();

    void readBytes(byte[] dst);

    int readerIndex();

    void setReaderIndex(int readerIndex);
}
