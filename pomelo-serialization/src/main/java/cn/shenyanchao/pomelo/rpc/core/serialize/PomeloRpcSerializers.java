package cn.shenyanchao.pomelo.rpc.core.serialize;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.shenyanchao.pomelo.rpc.core.serialize.hessian.HessianSerialization;
import cn.shenyanchao.pomelo.rpc.core.serialize.jdk.JdkSerialization;
import cn.shenyanchao.pomelo.rpc.core.serialize.kryo.KryoSerialization;
import cn.shenyanchao.pomelo.rpc.core.serialize.proto.ProtoBufSerialization;

/**
 * 编解码
 *
 * @author shenyanchao
 */
public class PomeloRpcSerializers {

    public static final int JAVA_CODEC = 1;

    public static final int HESSIAN_CODEC = 2;

    public static final int PB_CODEC = 3;

    public static final int KRYO_CODEC = 4;

    private static Map<Integer, Serialization> serializers = new ConcurrentHashMap<>(8);

    static {
        serializers.put(JAVA_CODEC, new JdkSerialization());
        serializers.put(HESSIAN_CODEC, new HessianSerialization());
        serializers.put(PB_CODEC, new ProtoBufSerialization());
        serializers.put(KRYO_CODEC, new KryoSerialization());
    }

    public static Serialization getSerialization(int serializerKey) {
        return serializers.get(serializerKey);
    }

}
