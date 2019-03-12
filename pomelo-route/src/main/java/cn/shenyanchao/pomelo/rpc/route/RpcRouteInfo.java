package cn.shenyanchao.pomelo.rpc.route;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import cn.shenyanchao.pomelo.rpc.core.server.intercepotr.RpcInterceptor;

/**
 * @author shenyanchao
 */
public class RpcRouteInfo implements Serializable {

    private static final long serialVersionUID = -8853603569468133779L;

    /**
     * url
     */
    private String route;

    /**
     * 方法名称
     */
    private String method;

    /**
     * 类型
     * post,get,head,put,delete
     */
    private String httpType;

    /**
     * 参数
     */
    private Map<String, Object> params;

    /**
     * 实现类
     */
    private Class<?> objCls;//

    /**
     * 方法
     */
    private Method[] methods;

    /**
     * 返回类型
     * text/plain json
     */
    private String returnType;

    /**
     * 过滤器
     */
    private RpcInterceptor rpcInterceptor;

    public RpcRouteInfo() {
        super();
    }

    /**
     * @return the route
     */
    public String getRoute() {
        return route;
    }

    /**
     * @param route the route to set
     */
    public void setRoute(String route) {
        this.route = route;
    }

    /**
     * @return the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * @param method the method to set
     */
    public void setMethod(String method) {
        this.method = method;
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
     * @return the params
     */
    public Map<String, Object> getParams() {
        return params;
    }

    /**
     * @param params the params to set
     */
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    /**
     * @return the objCls
     */
    public Class<?> getObjCls() {
        return objCls;
    }

    /**
     * @param objCls the objCls to set
     */
    public void setObjCls(Class<?> objCls) {
        this.objCls = objCls;
    }

    /**
     * @return the methods
     */
    public Method[] getMethods() {
        return methods;
    }

    /**
     * @param methods the methods to set
     */
    public void setMethods(Method[] methods) {
        this.methods = methods;
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

    public RpcInterceptor getRpcInterceptor() {
        return rpcInterceptor;
    }

    public void setRpcInterceptor(RpcInterceptor rpcInterceptor) {
        this.rpcInterceptor = rpcInterceptor;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RpcRouteInfo{");
        sb.append("route='").append(route).append('\'');
        sb.append(", method='").append(method).append('\'');
        sb.append(", httpType='").append(httpType).append('\'');
        sb.append(", params=").append(params);
        sb.append(", objCls=").append(objCls);
        sb.append(", methods=").append(methods == null ? "null" : Arrays.asList(methods).toString());
        sb.append(", returnType='").append(returnType).append('\'');
        sb.append(", rpcInterceptor=").append(rpcInterceptor);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RpcRouteInfo that = (RpcRouteInfo) o;
        return Objects.equals(route, that.route) &&
                Objects.equals(method, that.method) &&
                Objects.equals(httpType, that.httpType) &&
                Objects.equals(params, that.params) &&
                Objects.equals(objCls, that.objCls) &&
                Arrays.equals(methods, that.methods) &&
                Objects.equals(returnType, that.returnType) &&
                Objects.equals(rpcInterceptor, that.rpcInterceptor);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(route, method, httpType, params, objCls, returnType, rpcInterceptor);
        result = 31 * result + Arrays.hashCode(methods);
        return result;
    }
}
