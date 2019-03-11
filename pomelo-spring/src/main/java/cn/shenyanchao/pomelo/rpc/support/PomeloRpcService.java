package cn.shenyanchao.pomelo.rpc.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

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
        //        Map<String, Object> rpcBeans = applicationContext.getBeansWithAnnotation(PomeloRpc.class);
        //        for (Map.Entry entry : rpcBeans.entrySet()) {
        ////            System.out.println(entry.getKey() + "" + entry.getValue());
        //        }

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
