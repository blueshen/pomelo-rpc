package cn.shenyanchao.pomelo.rpc.core.route;

import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * 权重轮询server
 *
 * @author shenyanchao
 */
public class RpcRouteServer implements Serializable {

    private static final long serialVersionUID = -59907756387170272L;

    public InetSocketAddress server;
    public int weight;
    public int effectiveWeight;
    public int currentWeight;
    public boolean down;

    public RpcRouteServer(InetSocketAddress server, int weight) {
        this.server = server;
        this.weight = weight;
        this.effectiveWeight = this.weight;
        this.currentWeight = 0;
        if (this.weight < 0) {
            this.down = true;
        } else {
            this.down = false;
        }
    }

    /**
     * @return the server
     */
    public InetSocketAddress getServer() {
        return server;
    }

    /**
     * @param server the server to set
     */
    public void setServer(InetSocketAddress server) {
        this.server = server;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getEffectiveWeight() {
        return effectiveWeight;
    }

    public void setEffectiveWeight(int effectiveWeight) {
        this.effectiveWeight = effectiveWeight;
    }

    public int getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(int currentWeight) {
        this.currentWeight = currentWeight;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

}
