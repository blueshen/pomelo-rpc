package cn.shenyanchao.pomelo.rpc.tcp.netty4.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.inject.Singleton;

/**
 * @author shenyanchao
 * @since 2019-03-11 18:45
 */

@Singleton
public class ClientHolder {

    private Map<String, RpcClient> rpcClients = new ConcurrentHashMap<>();

    public RpcClient getRpcClient(String host, int port) {
        String key = genRpcClientKey(host, port);
        return rpcClients.get(key);
    }

    private String genRpcClientKey(String host, int port) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("/").append(host).append(":").append(port);
        return stringBuffer.toString();
    }

    public void clearClients() {
        rpcClients.clear();
    }

    public void putRpcClient(String host, int port, RpcClient rpcClient) {
        String key = genRpcClientKey(host, port);
        rpcClients.put(key, rpcClient);
    }

    public boolean containClient(String host, int port) {
        String key = genRpcClientKey(host, port);
        return rpcClients.containsKey(key);
    }

    public void removeRpcClient(String host, int port) {
        String key = genRpcClientKey(host, port);
        if (rpcClients.containsKey(key)) {
            rpcClients.remove(key);
        }
    }

    public void stopClient() {
        clearClients();
    }
}
