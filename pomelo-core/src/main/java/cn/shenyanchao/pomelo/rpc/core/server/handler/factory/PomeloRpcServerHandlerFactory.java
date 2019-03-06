package cn.shenyanchao.pomelo.rpc.core.server.handler.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.shenyanchao.pomelo.rpc.core.server.handler.http.AbstractRpcHttpServerHandler;
import cn.shenyanchao.pomelo.rpc.core.server.handler.http.RpcHttpServerHandler;
import cn.shenyanchao.pomelo.rpc.core.server.handler.tcp.AbstractRpcTcpServerHandler;
import cn.shenyanchao.pomelo.rpc.core.server.handler.tcp.RpcTcpServerHandler;

/**
 * server handler 工厂类
 *
 * @author shenyanchao
 */
public class PomeloRpcServerHandlerFactory {

    private static Map<Integer, AbstractRpcTcpServerHandler> serverHandlers = new ConcurrentHashMap<>(8);

    private static Map<Integer, AbstractRpcHttpServerHandler> httpServerHandlers = new ConcurrentHashMap<>(8);

    static {
        serverHandlers.put(RpcTcpServerHandler.TYPE, new RpcTcpServerHandler());
        httpServerHandlers.put(RpcHttpServerHandler.TYPE, new RpcHttpServerHandler());
    }

    public static AbstractRpcTcpServerHandler getServerHandler() {
        return serverHandlers.get(RpcTcpServerHandler.TYPE);
    }

    public static AbstractRpcHttpServerHandler getHttpServerHandler() {
        return httpServerHandlers.get(RpcHttpServerHandler.TYPE);
    }
}
