package cn.shenyanchao.pomelo.rpc.support;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import cn.shenyanchao.pomelo.rpc.discovery.DiscoveryModule;
import cn.shenyanchao.pomelo.rpc.registry.RegisterModule;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.client.factory.PomeloRpcTcpClientFactory;

/**
 * @author shenyanchao
 */
public class PomeloRpcApplication implements InitializingBean {

    private String address = null;

    private String clientId = null;

    /**
     * server :1
     * client :2
     */
    private Integer flag;

    private int timeout;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isBlank(address)) {
            throw new RuntimeException("address can not be null or empty");
        }
        if (null == flag) {
            throw new RuntimeException("flag can not be null or empty");
        }
        if (flag != 1 && flag != 2) {
            throw new RuntimeException("flag only be 1 or 2");
        }

        if (1 == flag) {
            //服务端
            RegisterModule.getInstance().initZooKeeper(address, timeout);
        } else if (2 == flag) {
            //客户端
            DiscoveryModule.getInstance().initZooKeeper(address, timeout);
            PomeloRpcTcpClientFactory.getInstance().connect(timeout);//客户端启动
        }

    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the timeout
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * @param timeout the timeout to set
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * @param flag the flag to set
     */
    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
