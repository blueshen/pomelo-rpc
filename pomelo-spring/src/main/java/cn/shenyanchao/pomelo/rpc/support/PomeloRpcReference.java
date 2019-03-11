package cn.shenyanchao.pomelo.rpc.support;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.shenyanchao.pomelo.rpc.serialize.PomeloSerializer;

/**
 * @author shenyanchao
 */

public class PomeloRpcReference {

    private static final Logger LOG = LoggerFactory.getLogger(PomeloRpcReference.class);
    /**
     * 接口名称
     */
    private String interfaceName;
    /**
     * 超时时间
     */
    private int timeout;
    /**
     * 序列化类型
     */
    private PomeloSerializer serializer;
    /**
     * 协议类型
     */
    private byte protocolType;
    /**
     * 组名
     */
    private String group;

    public Class getObjectType() {
        try {
            if (StringUtils.isBlank(interfaceName)) {
                LOG.warn("interfaceName is null");
                return null;
            } else {
                return Thread.currentThread().getContextClassLoader().loadClass(interfaceName);
            }

        } catch (ClassNotFoundException e) {
            LOG.error("spring parser error!", e);
        }
        return null;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
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

    public PomeloSerializer getSerializer() {
        return serializer;
    }

    public void setSerializer(PomeloSerializer serializer) {
        this.serializer = serializer;
    }

    public byte getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(byte protocolType) {
        this.protocolType = protocolType;
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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PomeloRpcReference{");
        sb.append("interfaceName='").append(interfaceName).append('\'');
        sb.append(", timeout=").append(timeout);
        sb.append(", serializer=").append(serializer);
        sb.append(", protocolType=").append(protocolType);
        sb.append(", group='").append(group).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
