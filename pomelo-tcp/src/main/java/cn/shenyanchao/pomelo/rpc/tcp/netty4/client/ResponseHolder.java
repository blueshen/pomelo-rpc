package cn.shenyanchao.pomelo.rpc.tcp.netty4.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import cn.shenyanchao.pomelo.rpc.core.message.PomeloResponseMessage;

/**
 * @author shenyanchao
 * @since 2019-03-11 12:17
 */

@Singleton
public class ResponseHolder {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseHolder.class);

    private Map<Integer, LinkedBlockingQueue<Object>> responses = new ConcurrentHashMap<>();

    public void putResponse(final int key, LinkedBlockingQueue<Object> queue) {
        responses.put(key, queue);
    }

    public void receiveResponse(PomeloResponseMessage response) {
        if (!responses.containsKey(response.getRequestId())) {
            LOG.error("give up the response, request id is: {},maybe timeout!", response.getRequestId());
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

    public void removeResponse(int key) {
        responses.remove(key);
    }
}
