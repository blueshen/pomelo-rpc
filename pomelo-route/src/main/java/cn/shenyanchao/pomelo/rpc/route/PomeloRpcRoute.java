package cn.shenyanchao.pomelo.rpc.route;

import java.util.List;

/**
 * @author shenyanchao
 */
public class PomeloRpcRoute {

    public static RpcRouteServer getBestServer(List<RpcRouteServer> serverList) {
        RpcRouteServer server;
        RpcRouteServer best = null;
        int total = 0;
        for (int i = 0, len = serverList.size(); i < len; i++) {

            server = serverList.get(i);
            if (server.down) {
                continue;
            }
            server.currentWeight += server.effectiveWeight;
            total += server.effectiveWeight;
            if (server.effectiveWeight < server.weight) {
                server.effectiveWeight++;
            }
            if (null == best || server.currentWeight > best.currentWeight) {
                best = server;
            }

        }
        if (best == null) {
            return null;
        }
        best.currentWeight -= total;
        return best;
    }

}
