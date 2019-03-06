package cn.shenyanchao.pomelo.rpc.tcp.netty4.client.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;
import java.util.Set;

import cn.shenyanchao.pomelo.rpc.core.client.RpcClient;
import cn.shenyanchao.pomelo.rpc.core.client.factory.RpcClientFactory;
import cn.shenyanchao.pomelo.rpc.core.route.RpcRouteServer;
import cn.shenyanchao.pomelo.rpc.core.util.SocketAddressUtil;
import cn.shenyanchao.pomelo.rpc.discovery.DiscoveryModule;

/**
 * @author shenyanchao
 */
public abstract class AbstractRpcClientInvocationHandler implements InvocationHandler {

    private String group;

    private int timeout;

    private String targetInstanceName;

    private int codecType;

    private int protocolType;

    public AbstractRpcClientInvocationHandler(
            String group, int timeout,
            String targetInstanceName, int codecType, int protocolType) {
        super();
        this.group = group;
        this.timeout = timeout;
        this.targetInstanceName = targetInstanceName;
        this.codecType = codecType;
        this.protocolType = protocolType;

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcClient client;

        String[] groups = group.split(",");
        Random r = new Random();
        int i = r.nextInt(groups.length);
        Set<InetSocketAddress> addresses = DiscoveryModule.getInstance().getActiveServersByGroup(groups[i]);
        List<RpcRouteServer> servers = SocketAddressUtil.getInetSocketAddress(addresses);
        if (servers.size() <= 0) {
            throw new Exception("no server found!  please check~");
        }
        r = new Random();
        int j = r.nextInt(servers.size());
        InetSocketAddress server = servers.get(j).getServer();

        client = getClientFactory().getRpcClient(server.getAddress().getHostAddress(), server.getPort());
        String methodName = method.getName();
        String[] argTypes = createParamSignature(method.getParameterTypes());
        Object result =
                client.invokeImpl(targetInstanceName, methodName, argTypes, args, timeout, codecType, protocolType);
        return result;
    }

    private String[] createParamSignature(Class<?>[] argTypes) {
        if (argTypes == null || argTypes.length == 0) {
            return new String[] {};
        }
        String[] paramSig = new String[argTypes.length];
        for (int x = 0; x < argTypes.length; x++) {
            paramSig[x] = argTypes[x].getName();
        }
        return paramSig;
    }

    public abstract RpcClientFactory getClientFactory();

}
