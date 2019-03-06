package cn.shenyanchao.pomelo.rpc.core.serialize.hessian;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import cn.shenyanchao.pomelo.rpc.core.serialize.Serialization;

/**
 * @author shenyanchao
 * @since 2019-03-06 21:19
 */
public class HessianSerialization implements Serialization {
    @Override
    public byte[] serialize(Object object) throws Exception {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        Hessian2Output output = new Hessian2Output(byteArray);
        output.writeObject(object);
        output.close();
        byte[] bytes = byteArray.toByteArray();
        return bytes;
    }

    @Override
    public Object deserialize(String className, byte[] bytes) throws Exception {

        Hessian2Input input = new Hessian2Input(new ByteArrayInputStream(bytes));
        Object resultObject = input.readObject();
        input.close();
        return resultObject;
    }

}
