package cn.shenyanchao.pomelo.rpc.http.netty4.server.handler;

import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import cn.shenyanchao.pomelo.rpc.core.server.handler.RpcServerHandler;
import cn.shenyanchao.pomelo.rpc.core.server.intercepotr.RpcInterceptor;
import cn.shenyanchao.pomelo.rpc.http.netty4.server.bean.RpcHttpBean;
import cn.shenyanchao.pomelo.rpc.route.PomeloRpcRouteService;
import cn.shenyanchao.pomelo.rpc.route.RpcRouteInfo;

/**
 * @author shenyanchao
 */

@Singleton
public class RpcHttpServerHandler implements RpcServerHandler {

    @Inject
    private PomeloRpcRouteService pomeloRpcRouteService;

    @Override
    public void addHandler(String instanceName, Object instance,
                           RpcInterceptor rpcFilter) {
        if (instance instanceof RpcHttpBean) {
            RpcHttpBean rpcHttpBean = (RpcHttpBean) instance;
            pomeloRpcRouteService.registerRoute(instanceName, rpcHttpBean.getObject(),
                    rpcFilter, rpcHttpBean.getHttpType(), rpcHttpBean.getReturnType());
        }
    }

    public RpcRouteInfo isRouteInfos(String route, String methodType,
                                     Map<String, Object> params) throws Exception {
        return pomeloRpcRouteService.isRouteInfos(route, methodType, params);
    }

    public Object methodInvoke(RpcRouteInfo routeInfo) throws Exception {
        return pomeloRpcRouteService.methodInvoke(routeInfo);
    }

    @Override
    public void clear() {
        pomeloRpcRouteService.clear();
    }

}
