package cn.shenyanchao.pomelo.rpc.discovery;

import java.net.InetSocketAddress;
import java.util.Set;

/**
 * @author shenyanchao
 */
public interface IDiscoveryModule {


    /**
     * 获取服务端 group 对应的server地址
     *
     * @param group
     * @return
     */
    Set<InetSocketAddress> getActiveServersByGroup(String group) throws Exception;

    /**
     * 关闭服务
     */
    void close() throws Exception;

    /**
     * 连接zk
     *
     * @param server
     * @param timeout
     * @throws Exception
     */
    void initZooKeeper(String server, int timeout) throws Exception;


}
