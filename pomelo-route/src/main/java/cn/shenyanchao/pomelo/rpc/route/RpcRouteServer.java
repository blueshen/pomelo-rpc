package cn.shenyanchao.pomelo.rpc.route;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * 权重轮询server
 *
 * @author shenyanchao
 */
public class RpcRouteServer implements Serializable {

    private static final long serialVersionUID = -59907756987170272L;

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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RpcRouteServer{");
        sb.append("server=").append(server);
        sb.append(", weight=").append(weight);
        sb.append(", effectiveWeight=").append(effectiveWeight);
        sb.append(", currentWeight=").append(currentWeight);
        sb.append(", down=").append(down);
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
        RpcRouteServer that = (RpcRouteServer) o;
        return weight == that.weight &&
                effectiveWeight == that.effectiveWeight &&
                currentWeight == that.currentWeight &&
                down == that.down &&
                Objects.equals(server, that.server);
    }

    @Override
    public int hashCode() {
        return Objects.hash(server, weight, effectiveWeight, currentWeight, down);
    }
}
