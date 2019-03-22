package cn.shenyanchao.pomelo.rpc.support;

/**
 * @author shenyanchao
 */
public class PomeloRpcHttpService {

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

}
