package cn.shenyanchao.pomelo.rpc.core.route.factory;

import cn.shenyanchao.pomelo.rpc.core.route.IPomeloRpcRouteService;
import cn.shenyanchao.pomelo.rpc.core.route.PomeloRpcRouteService;

/**
 * @author shenyanchao
 */
public class PomeloRpcRouteServiceFactory {

    private static IPomeloRpcRouteService routeService = new PomeloRpcRouteService();

    public static IPomeloRpcRouteService getRouteService() {
        return routeService;
    }
}
