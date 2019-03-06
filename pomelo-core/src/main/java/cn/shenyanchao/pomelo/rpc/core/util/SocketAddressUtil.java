package cn.shenyanchao.pomelo.rpc.core.util;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.shenyanchao.pomelo.rpc.core.route.RpcRouteServer;

public class SocketAddressUtil {

    public static List<RpcRouteServer> getInetSocketAddress(Set<InetSocketAddress> addresses) {
        List<RpcRouteServer> routeServers = new ArrayList<RpcRouteServer>();

        for (InetSocketAddress inetSocketAddress : addresses) {
            RpcRouteServer rpcRouteServer = new RpcRouteServer(inetSocketAddress, 1);
            routeServers.add(rpcRouteServer);
        }
        return routeServers;
    }
}
