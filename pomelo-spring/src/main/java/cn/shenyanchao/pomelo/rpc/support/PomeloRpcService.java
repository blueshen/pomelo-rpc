package cn.shenyanchao.pomelo.rpc.support;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import cn.shenyanchao.pomelo.rpc.core.server.filter.RpcInterceptor;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.server.PomeloTcpServer;

/**
 * @author shenyanchao
 */
public class PomeloRpcService implements ApplicationContextAware, ApplicationListener {

    /**
     * 接口名称 key
     */
    private String interfaceName;

    /**
     * 服务类bean value
     */
    private String ref;

    private ApplicationContext applicationContext;

    /**
     * 拦截器类
     */
    private String interceptorRef;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {

        if (StringUtils.isBlank(interceptorRef) || !(applicationContext
                                                             .getBean(interceptorRef) instanceof RpcInterceptor)) {
            PomeloTcpServer.getInstance().registerService(interfaceName, applicationContext.getBean(ref), null);
        } else {
            PomeloTcpServer.getInstance().registerService(interfaceName, applicationContext.getBean(ref),
                    (RpcInterceptor) applicationContext.getBean(interceptorRef));
        }
    }

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

    /**
     * @return the applicationContext
     */
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {

        this.applicationContext = applicationContext;
    }

    public String getInterceptorRef() {
        return interceptorRef;
    }

    public void setInterceptorRef(String interceptorRef) {
        this.interceptorRef = interceptorRef;
    }
}
