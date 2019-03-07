package cn.shenyanchao.pomelo.rpc.support;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import cn.shenyanchao.pomelo.rpc.core.server.filter.RpcInterceptor;
import cn.shenyanchao.pomelo.rpc.http.RpcHttpBean;
import cn.shenyanchao.pomelo.rpc.http.netty4.server.PomeloHttpServer;

/**
 * @author shenyanchao
 */
public class PomeloRpcHttpService implements ApplicationContextAware, ApplicationListener {

    private String projectName;

    private String ref;

    private String interceptorRef;

    /**
     * POST,GET,HEAD,PUT,DELETE
     */
    private String httpType;

    /**
     * Type:
     * html,json,xml
     */
    private String returnType;

    private ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {

        Object object = applicationContext.getBean(ref);
        if (StringUtils.isNotBlank(interceptorRef)) {
            RpcInterceptor rpcFilter = (RpcInterceptor) applicationContext.getBean(interceptorRef);
            RpcHttpBean rpcHttpBean = new RpcHttpBean(object, httpType, returnType);
            PomeloHttpServer.getInstance().registerService(projectName, rpcHttpBean, rpcFilter);
        } else {
            RpcHttpBean rpcHttpBean = new RpcHttpBean(object, httpType, returnType);
            PomeloHttpServer.getInstance().registerService(projectName, rpcHttpBean, null);
        }

    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    /**
     * @return the httpType
     */
    public String getHttpType() {
        return httpType;
    }

    /**
     * @param httpType the httpType to set
     */
    public void setHttpType(String httpType) {
        this.httpType = httpType;
    }

    /**
     * @return the returnType
     */
    public String getReturnType() {
        return returnType;
    }

    /**
     * @param returnType the returnType to set
     */
    public void setReturnType(String returnType) {
        this.returnType = returnType;
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

}
