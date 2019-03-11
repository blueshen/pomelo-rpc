package cn.shenyanchao.pomelo.rpc.route;

import java.util.Map;

import cn.shenyanchao.pomelo.rpc.core.server.intercepotr.RpcInterceptor;

/**
 * @author shenyanchao
 */
public interface IPomeloRpcRouteService {

    /**
     * @param route
     * @param methodType
     * @param params
     *
     * @return
     *
     * @throws Exception
     */
    RpcRouteInfo isRouteInfos(String route, String methodType, Map<String, Object> params) throws Exception;

    /**
     * 根据参数执行对应的方法
     *
     * @param routeInfo
     *
     * @return
     *
     * @throws Exception
     */
    Object methodInvoke(RpcRouteInfo routeInfo) throws Exception;

    /**
     * 注册路由
     *
     * @param instance
     */
    void registerRoute(String projectname, Object instance, RpcInterceptor rpcInterceptor, String httpType,
                       String returnType);

    void clear();
}
