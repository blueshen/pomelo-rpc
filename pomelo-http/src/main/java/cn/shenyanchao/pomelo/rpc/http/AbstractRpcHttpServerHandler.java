package cn.shenyanchao.pomelo.rpc.http;

import java.util.Map;

import cn.shenyanchao.pomelo.rpc.core.server.handler.RpcServerHandler;
import cn.shenyanchao.pomelo.rpc.route.RpcRouteInfo;

/**
 * @author shenyanchao
 */
public abstract class AbstractRpcHttpServerHandler implements RpcServerHandler {

    public abstract RpcRouteInfo isRouteInfos(String route, String methodType, Map<String, Object> params)
            throws Exception;

    public abstract Object methodInvoke(RpcRouteInfo routeInfo) throws Exception;
}
