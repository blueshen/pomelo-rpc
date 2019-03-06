package cn.shenyanchao.pomelo.rpc.core.serialize.kryo;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import cn.shenyanchao.pomelo.rpc.core.serialize.Serialization;
import cn.shenyanchao.pomelo.rpc.core.util.KryoUtils;

/**
 * @author shenyanchao
 * @since 2019-03-06 21:20
 */
public class KryoSerialization implements Serialization {

    private static final int BUFFER_SIZE = 4096;

    @Override
    public byte[] serialize(Object object) {

        Output output = new Output(BUFFER_SIZE);
        KryoUtils.getKryo().writeClassAndObject(output, object);
        return output.toBytes();
    }

    @Override
    public Object deserialize(String className, byte[] bytes) {

        Input input = new Input(bytes);
        return KryoUtils.getKryo().readClassAndObject(input);
    }
}
