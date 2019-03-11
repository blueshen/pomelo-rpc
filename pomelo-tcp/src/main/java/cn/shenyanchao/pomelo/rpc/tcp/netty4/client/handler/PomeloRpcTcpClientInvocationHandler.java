package cn.shenyanchao.pomelo.rpc.tcp.netty4.client.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;
import java.util.Set;

import cn.shenyanchao.pomelo.rpc.tcp.netty4.client.RpcClient;
import cn.shenyanchao.pomelo.rpc.discovery.DiscoveryModule;
import cn.shenyanchao.pomelo.rpc.route.RpcRouteServer;
import cn.shenyanchao.pomelo.rpc.route.SocketAddressUtil;
import cn.shenyanchao.pomelo.rpc.serialize.PomeloSerializer;
import cn.shenyanchao.pomelo.rpc.tcp.netty4.client.factory.PomeloRpcClientFactory;

/**
 * @author shenyanchao
 */

public class PomeloRpcTcpClientInvocationHandler implements InvocationHandler {

    private String group;

    private int timeout;

    private String targetInstanceName;

    private PomeloSerializer serializer;

    private DiscoveryModule discoveryModule;

    private PomeloRpcClientFactory pomeloRpcClientFactory;

    private byte protocolType;

    public PomeloRpcTcpClientInvocationHandler(
            String group, int timeout,
            String targetInstanceName, PomeloSerializer serializer, byte protocolType,
            DiscoveryModule discoveryModule, PomeloRpcClientFactory pomeloRpcClientFactory) {
        this.group = group;
        this.timeout = timeout;
        this.targetInstanceName = targetInstanceName;
        this.serializer = serializer;
        this.protocolType = protocolType;
        this.discoveryModule = discoveryModule;
        this.pomeloRpcClientFactory = pomeloRpcClientFactory;

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcClient client;

        String[] groups = group.split(",");
        Random r = new Random();
        int i = r.nextInt(groups.length);
        Set<InetSocketAddress> addresses = discoveryModule.getActiveServersByGroup(groups[i]);
        List<RpcRouteServer> servers = SocketAddressUtil.toInetSocketAddress(addresses);
        if (servers.size() <= 0) {
            throw new Exception("no server found! please check~");
        }

        //TODO ROUTE策略
        r = new Random();
        int j = r.nextInt(servers.size());
        InetSocketAddress server = servers.get(j).getServer();
        // TODO END
        client = pomeloRpcClientFactory
                .getRpcClient(server.getAddress().getHostAddress(), server.getPort());
        String methodName = method.getName();
        String[] argTypes = createParamSignature(method.getParameterTypes());
        Object result = client.invokeImpl(targetInstanceName, methodName, argTypes, args, timeout, serializer,
                protocolType);
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

}
