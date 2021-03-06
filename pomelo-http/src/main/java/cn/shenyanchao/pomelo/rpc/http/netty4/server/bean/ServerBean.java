package cn.shenyanchao.pomelo.rpc.http.netty4.server.bean;

import java.io.Serializable;
import java.util.Objects;

import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.DefaultHttpResponse;

/**
 * @author shenyanchao
 */
public class ServerBean implements Serializable {

    private static final long serialVersionUID = 1900069789009L;

    private DefaultHttpResponse defaultHttpResponse;

    private DefaultHttpContent defaultHttpContent;

    private boolean keepAlive;

    public ServerBean(DefaultHttpResponse response,
                      DefaultHttpContent defaultHttpContent, boolean keepAlive) {
        super();
        this.defaultHttpResponse = response;
        this.defaultHttpContent = defaultHttpContent;
        this.keepAlive = keepAlive;
    }

    public DefaultHttpResponse getDefaultHttpResponse() {
        return defaultHttpResponse;
    }

    public DefaultHttpContent getDefaultHttpContent() {
        return defaultHttpContent;
    }

    /**
     * @return the keepAlive
     */
    public boolean isKeepAlive() {
        return keepAlive;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ServerBean{");
        sb.append("defaultHttpResponse=").append(defaultHttpResponse);
        sb.append(", defaultHttpContent=").append(defaultHttpContent);
        sb.append(", keepAlive=").append(keepAlive);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServerBean that = (ServerBean) o;
        return keepAlive == that.keepAlive &&
                Objects.equals(defaultHttpResponse, that.defaultHttpResponse) &&
                Objects.equals(defaultHttpContent, that.defaultHttpContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(defaultHttpResponse, defaultHttpContent, keepAlive);
    }
}
