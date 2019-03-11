package cn.shenyanchao.pomelo.rpc.client;

import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import cn.shenyanchao.pomelo.rpc.demo.entity.RpcUser;
import okhttp3.ConnectionPool;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpServiceConsumer {
    public static final MediaType MediaType_JSON
            = MediaType.parse("application/json; charset=utf-8");
    private static final Logger LOG = LoggerFactory.getLogger(HttpServiceConsumer.class);
    ConnectionPool connectionPool = new ConnectionPool(1000, 60, TimeUnit.SECONDS);
    private OkHttpClient client = new OkHttpClient.Builder().connectionPool(connectionPool).build();

    @Test
    @Ignore
    public void mainHttpGetTest() throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("start test......");
        }
        int count = 10000;
        long begin = System.currentTimeMillis();
        Request request = new Request.Builder()
                .url("http://127.0.0.1:10009/helloGet/helloService/sayHi?name=shenyanchao")
                .build();

        for (int i = 0; i < count; i++) {
            Response response = client.newCall(request).execute();
            String returnValue = response.body().string();
            if (LOG.isDebugEnabled()) {
                LOG.debug(returnValue);
            }
        }
        long endtime = System.currentTimeMillis();
        LOG.info("get qps:{}", count / ((endtime - begin) / 1000.0));

    }

    @Test
    @Ignore
    public void mainHttpPostTest() throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("start test......");
        }
        long begin = System.currentTimeMillis();
        int count = 10000;
        RpcUser rpcUser = new RpcUser();
        rpcUser.setName("shen");
        rpcUser.setAge("30");
        RequestBody body = RequestBody.create(MediaType_JSON, JSON.toJSONString(rpcUser));
        Request request = new Request.Builder()
                .url("http://127.0.0.1:10009/hello/helloService/sayHiToUser")
                .post(body)
                .build();

        for (int i = 0; i < count; i++) {
            Response response = client.newCall(request).execute();
            String returnValue = response.body().string();
            if (LOG.isDebugEnabled()) {
                LOG.debug(returnValue);
            }

        }
        long endtime = System.currentTimeMillis();
        LOG.info("post qps:{}", count / ((endtime - begin) / 1000.0));
    }

}