package cn.shenyanchao.pomelo.rpc.core.serialize.jdk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cn.shenyanchao.pomelo.rpc.core.serialize.Serialization;

/**
 * @author shenyanchao
 * @since 2019-03-06 19:01
 */
public class JdkSerialization implements Serialization {

    @Override
    public byte[] serialize(Object object) throws Exception {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(byteArray);
        output.writeObject(object);
        output.flush();
        output.close();
        return byteArray.toByteArray();
    }

    @Override
    public Object deserialize(String className, byte[] bytes) throws Exception {

        ObjectInputStream objectIn = new ObjectInputStream(new ByteArrayInputStream(bytes));
        Object resultObject = objectIn.readObject();
        objectIn.close();
        return resultObject;
    }
}
