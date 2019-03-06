package cn.shenyanchao.pomelo.rpc.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import cn.shenyanchao.pomelo.rpc.core.server.filter.RpcFilter;
import cn.shenyanchao.pomelo.rpc.core.server.handler.http.RpcHttpBean;
import cn.shenyanchao.pomelo.rpc.http.netty4.server.PomeloHttpServer;

/**
 * @author shenyanchao
 */
public class PomeloRpcHttpService implements ApplicationContextAware, ApplicationListener {

    private String projectName;

    private String ref;

    private String filterRef;

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
        if (filterRef != null && !"".equals(filterRef)) {
            RpcFilter rpcFilter = (RpcFilter) applicationContext.getBean(filterRef);
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

    /**
     * @return the filterRef
     */
    public String getFilterRef() {
        return filterRef;
    }

    /**
     * @param filterRef the filterRef to set
     */
    public void setFilterRef(String filterRef) {
        this.filterRef = filterRef;
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
