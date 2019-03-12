package cn.shenyanchao.pomelo.rpc.route.utils;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.shenyanchao.pomelo.rpc.route.RpcRouteServer;

/**
 * @author shenyanchao
 */
public class SocketAddressUtil {

    public static List<RpcRouteServer> toInetSocketAddress(Set<InetSocketAddress> addresses) {
        List<RpcRouteServer> routeServers = new ArrayList<>();

        for (InetSocketAddress inetSocketAddress : addresses) {
            RpcRouteServer rpcRouteServer = new RpcRouteServer(inetSocketAddress, 1);
            routeServers.add(rpcRouteServer);
        }
        return routeServers;
    }
}
