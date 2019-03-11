package cn.shenyanchao.pomelo.rpc.support;

/**
 * @author shenyanchao
 */
public class PomeloRpcService {

    /**
     * 接口名称 key
     */
    private String interfaceName;

    /**
     * 服务类bean value
     */
    private String ref;

    /**
     * 拦截器类
     */
    private String interceptorRef;

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    /**
     * @return the ref
     */
    public String getRef() {
        return ref;
    }

    /**
     * @param ref the ref to set
     */
    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getInterceptorRef() {
        return interceptorRef;
    }

    public void setInterceptorRef(String interceptorRef) {
        this.interceptorRef = interceptorRef;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PomeloRpcService{");
        sb.append("interfaceName='").append(interfaceName).append('\'');
        sb.append(", ref='").append(ref).append('\'');
        sb.append(", interceptorRef='").append(interceptorRef).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
