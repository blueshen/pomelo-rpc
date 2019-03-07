package cn.shenyanchao.pomelo.rpc.serialize;

import cn.shenyanchao.pomelo.rpc.serialize.hessian.HessianSerialization;
import cn.shenyanchao.pomelo.rpc.serialize.jdk.JdkSerialization;
import cn.shenyanchao.pomelo.rpc.serialize.kryo.KryoSerialization;
import cn.shenyanchao.pomelo.rpc.serialize.proto.ProtoBufSerialization;

/**
 * @author shenyanchao
 * @since 2019-03-07 17:25
 */
public enum SerializerType {

    JAVA_CODEC((byte) 1, "java", new JdkSerialization()),
    HESSIAN_CODEC((byte) 2, "hessian", new HessianSerialization()),
    PB_CODEC((byte) 3, "protobuf", new ProtoBufSerialization()),
    KRYO_CODEC((byte) 4, "kryo", new KryoSerialization());

    private byte code;
    private String name;
    private Serialization serialization;

    SerializerType(byte code, String name, Serialization serialization) {
        this.code = code;
        this.name = name;
        this.serialization = serialization;
    }

    public static SerializerType parse(int code) {
        byte innerCode = (byte) code;
        for (SerializerType type : SerializerType.values()) {
            if (innerCode == type.code) {
                return type;
            }
        }
        return null;
    }

    public byte getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Serialization getSerialization() {
        return serialization;
    }
}
