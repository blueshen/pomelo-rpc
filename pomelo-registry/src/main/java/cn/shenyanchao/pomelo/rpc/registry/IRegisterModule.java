package cn.shenyanchao.pomelo.rpc.registry;

/**
 * @author shenyanchao
 */
public interface IRegisterModule {

    /**
     * 关闭服务节点
     */
    void close() throws Exception;

    /**
     * 连接zk
     *
     * @param zkServer
     * @param timeout
     *
     * @throws Exception
     */
    void initZooKeeper(String zkServer, int timeout) throws Exception;

    /**
     * 注册服务节点
     *
     * @param group  组名
     * @param server 机器
     * @param port   端口
     */
    void registerServer(String group, String server, int port) throws Exception;
}
