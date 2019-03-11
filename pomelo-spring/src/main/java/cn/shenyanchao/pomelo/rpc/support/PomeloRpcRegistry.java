package cn.shenyanchao.pomelo.rpc.support;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import cn.shenyanchao.pomelo.rpc.serialize.PomeloSerializer;
import cn.shenyanchao.pomelo.rpc.util.NetUtils;

/**
 * 注册
 *
 * @author shenyanchao
 */
public class PomeloRpcRegistry implements InitializingBean {

    private String ip;

    private int port;

    private int timeout;

    private byte protocolType;

    private PomeloSerializer serializer;

    private String group;

    private int threadCount;

    @Override
    public void afterPropertiesSet() throws Exception {

        if (port == 0) {
            throw new Exception("parameter port can not be null");
        }
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public byte getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(byte protocolType) {
        this.protocolType = protocolType;
    }

    public PomeloSerializer getSerializer() {
        return serializer;
    }

    public void setSerializer(PomeloSerializer serializer) {
        this.serializer = serializer;
    }

    public String getLocalhost() {
        String ip = StringUtils.isBlank(this.getIp()) ? NetUtils.getLocalHost() : this.getIp();
        return ip;
    }

    /**
     * @return the group
     */
    public String getGroup() {
        return group;
    }

    /**
     * @param group the group to set
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * @return the threadCount
     */
    public int getThreadCount() {
        return threadCount;
    }

    /**
     * @param threadCount the threadCount to set
     */
    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PomeloRpcRegistry{");
        sb.append("ip='").append(ip).append('\'');
        sb.append(", port=").append(port);
        sb.append(", timeout=").append(timeout);
        sb.append(", protocolType=").append(protocolType);
        sb.append(", serializer=").append(serializer);
        sb.append(", group='").append(group).append('\'');
        sb.append(", threadCount=").append(threadCount);
        sb.append('}');
        return sb.toString();
    }
}
