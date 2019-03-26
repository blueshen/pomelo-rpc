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

    private String route;

    private String method;

    /**
     * 类型
     * post,get,head,put,delete
     */
    private String httpType;

    private Map<String, Object> params;

    private Class<?> objCls;

    private Method[] methods;

    private String returnType;

    private RpcInterceptor rpcInterceptor;

    public RpcRouteInfo() {
        super();
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHttpType() {
        return httpType;
    }

    public void setHttpType(String httpType) {
        this.httpType = httpType;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public Class<?> getObjCls() {
        return objCls;
    }

    public void setObjCls(Class<?> objCls) {
        this.objCls = objCls;
    }

    public Method[] getMethods() {
        return methods;
    }

    public void setMethods(Method[] methods) {
        this.methods = methods;
    }

    public String getReturnType() {
        return returnType;
    }

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
