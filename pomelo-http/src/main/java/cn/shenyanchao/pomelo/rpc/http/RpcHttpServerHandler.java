package cn.shenyanchao.pomelo.rpc.http;

import java.util.Map;

import cn.shenyanchao.pomelo.rpc.core.route.RpcRouteInfo;
import cn.shenyanchao.pomelo.rpc.core.route.factory.PomeloRpcRouteServiceFactory;
import cn.shenyanchao.pomelo.rpc.core.server.filter.RpcInterceptor;

/**
 * @author shenyanchao
 */
public class RpcHttpServerHandler extends AbstractRpcHttpServerHandler {

    public static final int TYPE = 0;

    public static RpcHttpServerHandler getInstance() {
        return SingletonHolder.rpcHttpServerHandler;
    }

    @Override
    public void addHandler(String instanceName, Object instance,
                           RpcInterceptor rpcFilter) {
        if (instance instanceof RpcHttpBean) {
            RpcHttpBean rpcHttpBean = (RpcHttpBean) instance;
            PomeloRpcRouteServiceFactory.getRouteService().registerRoute(instanceName, rpcHttpBean.getObject(),
                    rpcFilter, rpcHttpBean.getHttpType(), rpcHttpBean.getReturnType());
        }
    }

    @Override
    public RpcRouteInfo isRouteInfos(String route, String methodType,
                                     Map<String, Object> params) throws Exception {
        return PomeloRpcRouteServiceFactory.getRouteService().isRouteInfos(route, methodType, params);
    }

    @Override
    public Object methodInvoke(RpcRouteInfo routeInfo) throws Exception {
        return PomeloRpcRouteServiceFactory.getRouteService().methodInvoke(routeInfo);
    }

    @Override
    public void clear() {
        PomeloRpcRouteServiceFactory.getRouteService().clear();
    }

    static class SingletonHolder {
        public static RpcHttpServerHandler rpcHttpServerHandler = new RpcHttpServerHandler();
    }
}
