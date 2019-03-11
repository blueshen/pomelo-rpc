package cn.shenyanchao.pomelo.rpc.support;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import cn.shenyanchao.pomelo.rpc.http.netty4.server.PomeloHttpServer;

/**
 * @author shenyanchao
 */
public class PomeloRpcHttpRegistry implements InitializingBean, DisposableBean {

    private int port;

    private int timeout;

    private PomeloHttpServer pomeloHttpServer;

    @Override
    public void destroy() throws Exception {
        if (null != pomeloHttpServer) {
            pomeloHttpServer.stop();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pomeloHttpServer = new PomeloHttpServer();
        pomeloHttpServer.run(port, timeout);
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the timeout
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * @param timeout the timeout to set
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

}
