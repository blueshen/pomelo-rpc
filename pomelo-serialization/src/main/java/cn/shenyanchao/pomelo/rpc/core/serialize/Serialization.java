package cn.shenyanchao.pomelo.rpc.core.serialize;

/**
 * @author shenyanchao
 * @since 2019-03-06 18:58
 */
public interface Serialization {

    byte[] serialize(Object object) throws Exception;
    /**
     * decode byte[] to Object
     */
    Object deserialize(String className, byte[] bytes) throws Exception;
}
