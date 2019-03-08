package cn.shenyanchao.pomelo.rpc.serialize;

import cn.shenyanchao.pomelo.rpc.serialize.hessian.HessianSerialization;
import cn.shenyanchao.pomelo.rpc.serialize.jdk.JdkSerialization;
import cn.shenyanchao.pomelo.rpc.serialize.kryo.KryoSerialization;
import cn.shenyanchao.pomelo.rpc.serialize.proto.ProtoBufSerialization;

/**
 * @author shenyanchao
 * @since 2019-03-07 17:25
 */
public enum PomeloSerializer {

    JAVA((byte) 1, new JdkSerialization()),
    HESSIAN((byte) 2, new HessianSerialization()),
    PROTOBUF((byte) 3, new ProtoBufSerialization()),
    KRYO((byte) 4, new KryoSerialization());

    private byte type;
    private Serialization serialization;

    PomeloSerializer(byte type, Serialization serialization) {
        this.type = type;
        this.serialization = serialization;
    }

    public static PomeloSerializer parse(byte type) {
        for (PomeloSerializer serializer : PomeloSerializer.values()) {
            if (type == serializer.type) {
                return serializer;
            }
        }
        return null;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public Serialization getSerialization() {
        return serialization;
    }

}
