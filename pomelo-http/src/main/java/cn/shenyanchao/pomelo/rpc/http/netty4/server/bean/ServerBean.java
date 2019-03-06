package cn.shenyanchao.pomelo.rpc.http.netty4.server.bean;

import java.io.Serializable;

import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.DefaultHttpResponse;

/**
 * @author shenyanchao
 */
public class ServerBean implements Serializable {

    private static final long serialVersionUID = 1062069789009L;

    private DefaultHttpResponse response;

    private DefaultHttpContent defaultHttpContent;

    private boolean keepAlive;

    public ServerBean(DefaultHttpResponse response,
                      DefaultHttpContent defaultHttpContent, boolean keepAlive) {
        super();
        this.response = response;
        this.defaultHttpContent = defaultHttpContent;
        this.keepAlive = keepAlive;
    }

    /**
     * @return the response
     */
    public DefaultHttpResponse getResponse() {
        return response;
    }

    /**
     * @param response the response to set
     */
    public void setResponse(DefaultHttpResponse response) {
        this.response = response;
    }

    /**
     * @return the defaultHttpContent
     */
    public DefaultHttpContent getDefaultHttpContent() {
        return defaultHttpContent;
    }

    /**
     * @param defaultHttpContent the defaultHttpContent to set
     */
    public void setDefaultHttpContent(DefaultHttpContent defaultHttpContent) {
        this.defaultHttpContent = defaultHttpContent;
    }

    /**
     * @return the keepAlive
     */
    public boolean isKeepAlive() {
        return keepAlive;
    }

    /**
     * @param keepAlive the keepAlive to set
     */
    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

}
