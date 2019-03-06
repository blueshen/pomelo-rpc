package cn.shenyanchao.pomelo.rpc.core.client.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.shenyanchao.pomelo.rpc.core.client.AbstractRpcClient;
import cn.shenyanchao.pomelo.rpc.core.client.RpcClient;
import cn.shenyanchao.pomelo.rpc.core.message.PomeloResponseMessage;

/**
 * @author shenyanchao
 */
public abstract class AbstractRpcClientFactory implements RpcClientFactory {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractRpcClientFactory.class);

    protected static ConcurrentHashMap<Integer, LinkedBlockingQueue<Object>> responses = new ConcurrentHashMap<>();
    protected static Map<String, AbstractRpcClient> rpcClients = new ConcurrentHashMap<>();

    @Override
    public RpcClient getRpcClient(String host, int port) throws Exception {
        String key = genRpcClientKey(host, port);
        if (rpcClients.containsKey(key)) {
            return rpcClients.get(key);
        }
        return createClient(host, port);
    }

    private String genRpcClientKey(String host, int port) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("/").append(host).append(":").append(port);
        return stringBuffer.toString();
    }

    protected abstract RpcClient createClient(String targetIP, int targetPort) throws Exception;

    @Override
    public void putResponse(final int key, LinkedBlockingQueue<Object> queue) {
        responses.put(key, queue);
    }

    /**
     * 停止客户端
     */
    public abstract void stopClient() throws Exception;

    @Override
    public void receiveResponse(PomeloResponseMessage response) throws Exception {
        if (!responses.containsKey(response.getRequestId())) {
            LOG.error("give up the response,request id is: {} ,maybe timeout!", response.getRequestId());
            return;
        }
        try {

            if (responses.containsKey(response.getRequestId())) {
                LinkedBlockingQueue<Object> queue = responses.get(response.getRequestId());
                if (queue != null) {
                    queue.put(response);
                } else {
                    LOG.warn("give up the response,request id is:{}, because queue is null", response.getRequestId());
                }
            }

        } catch (InterruptedException e) {
            LOG.error("put response error,request id is:{}", response.getRequestId(), e);
        }

    }

    @Override
    public void removeResponse(int key) {
        responses.remove(key);
    }

    public void clearClients() {
        rpcClients.clear();
    }

    @Override
    public void putRpcClient(String host, int port, AbstractRpcClient rpcClient) {
        String key = genRpcClientKey(host, port);
        rpcClients.putIfAbsent(key, rpcClient);
    }

    @Override
    public boolean containClient(String host, int port) {
        String key = genRpcClientKey(host, port);
        return rpcClients.containsKey(key);
    }

    @Override
    public void removeRpcClient(String host, int port) {
        String key = genRpcClientKey(host, port);
        if (rpcClients.containsKey(key)) {
            rpcClients.remove(key);
        }
    }
}
