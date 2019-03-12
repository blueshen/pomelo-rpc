package cn.shenyanchao.pomelo.rpc.http.netty4.server.handler;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.inject.Inject;

import cn.shenyanchao.pomelo.rpc.http.RpcHttpServerHandler;
import cn.shenyanchao.pomelo.rpc.http.netty4.server.bean.ServerBean;
import cn.shenyanchao.pomelo.rpc.route.RpcRouteInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.DefaultLastHttpContent;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * @author shenyanchao
 */
public class PomeloHttpServerHandler extends ChannelInboundHandlerAdapter {

    @Inject
    private RpcHttpServerHandler rpcHttpServerHandler;

    public PomeloHttpServerHandler() {
        super();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.channel().close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        handleRequestWithSingleThread(ctx, msg);
    }

    private void handleRequestWithSingleThread(ChannelHandlerContext ctx, Object message) {

        boolean keepAlive = true;
        try {

            ServerBean serverBean;
            Map<String, Object> map = new HashMap<>(16);
            String httpType = null;
            String url = null;
            if (message instanceof HttpRequest) {
                HttpRequest request = (HttpRequest) message;
                if (HttpUtil.is100ContinueExpected(request)) {
                    DefaultHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.CONTINUE);
                    serverBean = new ServerBean(response, null, keepAlive);
                    writeResponse(ctx, serverBean);
                    return;
                }
                httpType = request.method().name();
                url = request.uri();
                url = url.substring(1);
                if (url.contains("?")) {
                    url = url.substring(0, url.indexOf("?"));
                }
                keepAlive = HttpUtil.isKeepAlive(request);
                QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
                map.putAll(getParametersByUrl(queryStringDecoder));
            }
            if (message instanceof HttpContent) {
                HttpContent httpContent = (HttpContent) message;
                ByteBuf content = httpContent.content();
                if (content.isReadable()) {
                    String contentMsg = content.toString(CharsetUtil.UTF_8);
                    if (StringUtils.isNotBlank(contentMsg)) {
                        Map<String, Object> params = JSONObject.parseObject(contentMsg, Map.class);
                        map.putAll(params);
                    }

                }
                if (message instanceof LastHttpContent) {
                    RpcRouteInfo rpcRouteInfo =
                            rpcHttpServerHandler.isRouteInfos(url, httpType, map);
                    if (rpcRouteInfo == null || rpcRouteInfo.getRoute() == null) {
                        DefaultHttpResponse response = getDefaultHttpResponse(HttpResponseStatus.NOT_FOUND, null);
                        serverBean = new ServerBean(response, null, keepAlive);
                        writeResponse(ctx, serverBean);
                    } else {
                        DefaultHttpResponse response = getDefaultHttpResponse(HttpResponseStatus.OK,
                                rpcRouteInfo.getReturnType());
                        Object result = rpcHttpServerHandler.methodInvoke(rpcRouteInfo);
                        if (result == null) {
                            serverBean = new ServerBean(response, null, keepAlive);
                            writeResponse(ctx, serverBean);
                        } else {
                            String resultMsg = JSONObject.toJSONString(result);
                            DefaultHttpContent defaultHttpContent =
                                    new DefaultHttpContent(Unpooled.copiedBuffer(resultMsg, CharsetUtil.UTF_8));
                            serverBean = new ServerBean(response, defaultHttpContent, keepAlive);
                            writeResponse(ctx, serverBean);
                        }

                    }
                }
            }
        } catch (Exception e) {
            DefaultHttpResponse response = getDefaultHttpResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR, null);
            DefaultHttpContent defaultHttpContent =
                    new DefaultHttpContent(Unpooled.copiedBuffer(e.getMessage(), CharsetUtil.UTF_8));
            ServerBean serverBean = new ServerBean(response, defaultHttpContent, keepAlive);
            writeResponse(ctx, serverBean);
        } finally {
            ReferenceCountUtil.release(message);
        }

    }

    private Map<String, Object> getParametersByUrl(QueryStringDecoder queryStringDecoder) {
        Map<String, Object> map = new HashMap<>();
        Map<String, List<String>> params = queryStringDecoder.parameters();
        if (!params.isEmpty()) {
            for (Entry<String, List<String>> p : params.entrySet()) {
                String key = p.getKey();
                List<String> vals = p.getValue();
                for (String val : vals) {
                    map.put(key, val);
                }
            }

        }
        return map;
    }

    private DefaultHttpResponse getDefaultHttpResponse(HttpResponseStatus type, String returnType) {
        DefaultHttpResponse httpResponse;
        if (HttpResponseStatus.NOT_FOUND == type) {
            httpResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
        } else if (HttpResponseStatus.INTERNAL_SERVER_ERROR == type) {
            httpResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        } else {
            httpResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        }
        httpResponse.headers().add(HttpHeaderNames.TRANSFER_ENCODING,
                HttpHeaderValues.CHUNKED);
        if (HttpResponseStatus.NOT_FOUND != type && HttpResponseStatus.INTERNAL_SERVER_ERROR != type) {
            if ("json".equalsIgnoreCase(returnType)) {
                httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
            } else if ("html".equalsIgnoreCase(returnType)) {
                httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
            } else if ("xml".equalsIgnoreCase(returnType)) {
                httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/xml; charset=UTF-8");
            }
        } else {
            httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        }

        httpResponse.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        httpResponse.headers().set(HttpHeaderNames.CACHE_CONTROL, "no-cache");
        httpResponse.headers().set(HttpHeaderNames.PRAGMA, "no-cache");
        httpResponse.headers().set(HttpHeaderNames.EXPIRES, "-1");
        return httpResponse;
    }

    private void writeResponse(ChannelHandlerContext ctx, ServerBean serverBean) {

        if (ctx.channel().isOpen() && serverBean != null) {
            if (!serverBean.isKeepAlive()) {
                ctx.write(serverBean.getDefaultHttpResponse()).addListener(ChannelFutureListener.CLOSE);
            } else {
                serverBean.getDefaultHttpResponse().headers()
                        .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                ctx.write(serverBean.getDefaultHttpResponse());
            }
            if (serverBean.getDefaultHttpContent() != null) {
                ctx.writeAndFlush(serverBean.getDefaultHttpContent());
            }
        }

        DefaultLastHttpContent lastHttpContent = new DefaultLastHttpContent();
        ctx.writeAndFlush(lastHttpContent);
    }
}
