package cn.shenyanchao.pomelo.rpc.core.route;

import cn.shenyanchao.pomelo.rpc.core.server.filter.RpcFilter;

import java.util.Map;

/**
 * @author shenyanchao
 */
public interface IPomeloRpcRouteService {

    /**
     * 判断请求地址是否包含在路由表内，有则返回对应的实体
     * 如果有，设置参数;没有不设置，返回null
     *
     * @param route
     * @param methodType
     * @param parmas
     * @return
     * @throws Exception
     */
    RpcRouteInfo isRouteInfos(String route, String methodType, Map<String, Object> parmas) throws Exception;

    /**
     * 根据参数执行对应的方法
     *
     * @param routeInfo
     * @return
     * @throws Exception
     */
    Object methodInvoke(RpcRouteInfo routeInfo) throws Exception;

    /**
     * 注册路由
     *
     * @param instance
     */
    void registerRoute(String projectname, Object instance, RpcFilter rpcFilter, String httpType, String returnType);


    void clear();
}
