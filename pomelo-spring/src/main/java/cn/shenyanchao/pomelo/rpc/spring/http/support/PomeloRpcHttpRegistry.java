package cn.shenyanchao.pomelo.rpc.spring.http.support;

/**
 * @author shenyanchao
 */
public class PomeloRpcHttpRegistry {

    private int port;

    private int timeout;

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
