package cn.shenyanchao.pomelo.rpc.serialize;

/**
 * @author shenyanchao
 * @since 2019-03-06 18:58
 */
public interface Serialization {

    /**
     * @param object
     *
     * @return
     *
     * @throws Exception
     */
    byte[] serialize(Object object) throws Exception;

    /**
     * @param className
     * @param bytes
     *
     * @return
     *
     * @throws Exception
     */
    Object deserialize(String className, byte[] bytes) throws Exception;
}
