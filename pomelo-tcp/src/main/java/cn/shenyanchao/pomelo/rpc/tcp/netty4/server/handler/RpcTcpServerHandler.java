package cn.shenyanchao.pomelo.rpc.tcp.netty4.server.handler;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.shenyanchao.pomelo.rpc.core.message.PomeloRequestMessage;
import cn.shenyanchao.pomelo.rpc.core.message.PomeloResponseMessage;
import cn.shenyanchao.pomelo.rpc.core.server.filter.RpcInterceptor;
import cn.shenyanchao.pomelo.rpc.serialize.SerializerType;

/**
 * 服务端处理请求类
 *
 * @author shenyanchao
 */
public class RpcTcpServerHandler extends AbstractRpcTcpServerHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RpcTcpServerHandler.class);

    /**
     * 保存处理bean实例
     */
    private static Map<String, RpcFilterServerBean> rpcBeanHandlers = new ConcurrentHashMap<>(128);

    private static Map<String, Method> cacheMethods = new ConcurrentHashMap<>(256);

    public static RpcTcpServerHandler getInstance() {
        return SingletonHolder.rpcTcpServerHandler;
    }

    @Override
    public void addHandler(String instanceName, Object instance, RpcInterceptor rpcFilter) {
        RpcFilterServerBean filterServerBean = new RpcFilterServerBean(instance, rpcFilter);

        rpcBeanHandlers.put(instanceName, filterServerBean);
        Class<?> instanceClass = instance.getClass();
        Method[] methods = instanceClass.getDeclaredMethods();
        for (Method method : methods) {
            Class[] classArgTypes = method.getParameterTypes();
            String[] argTypes = new String[classArgTypes.length];
            for (int i = 0; i < classArgTypes.length; i++) {
                argTypes[i] = classArgTypes[i].getName();
            }
            String methodKey = getMethodKey(instanceName, method.getName(), argTypes);
            cacheMethods.put(methodKey, method);
        }
    }

    @Override
    public PomeloResponseMessage handleRequest(PomeloRequestMessage request, int codecType, int protocolType) {
        PomeloResponseMessage responseWrapper = new PomeloResponseMessage(request.getId(), codecType, protocolType);
        String targetInstanceName = new String(request.getTargetInstanceName());
        String methodName = new String(request.getMethodName());
        byte[][] argTypeBytes = request.getArgTypes();
        String[] argTypes = new String[argTypeBytes.length];
        for (int i = 0; i < argTypeBytes.length; i++) {
            argTypes[i] = new String(argTypeBytes[i]);
        }
        Object[] requestObjects;
        Method method;
        try {
            RpcFilterServerBean rpcFilterServerBean = rpcBeanHandlers.get(targetInstanceName);
            if (rpcFilterServerBean == null) {
                throw new Exception("no " + targetInstanceName + " instance exists on the server");
            }
            if (null != argTypes && argTypes.length > 0) {
                String methodKey = getMethodKey(targetInstanceName, methodName, argTypes);
                requestObjects = new Object[argTypes.length];
                method = cacheMethods.get(methodKey);
                if (method == null) {
                    throw new Exception(
                            "no method: " + methodKey + " find in " + targetInstanceName + " on the server");
                }
                Object[] tmprequestObjects = request.getRequestObjects();
                for (int i = 0; i < tmprequestObjects.length; i++) {
                    try {
                        requestObjects[i] = SerializerType.parse(request.getSerializerType()).getSerialization()
                                .deserialize(argTypes[i], (byte[]) tmprequestObjects[i]);
                    } catch (Exception e) {
                        throw new Exception("decode request object args error", e);
                    }
                }
            } else {
                method = rpcFilterServerBean.getObject().getClass().getMethod(methodName,
                        new Class<?>[] {});
                if (method == null) {
                    throw new Exception(
                            "no method: " + methodName + " find in " + targetInstanceName + " on the server");
                }
                requestObjects = new Object[] {};
            }
            method.setAccessible(true);
            if (rpcFilterServerBean.getRpcFilter() != null) {
                // filter 过滤
                RpcInterceptor rpcFilter = rpcFilterServerBean.getRpcFilter();
                if (rpcFilter.before(method, rpcFilterServerBean.getObject(), requestObjects)) {
                    responseWrapper.setResponse(method.invoke(rpcFilterServerBean.getObject(), requestObjects));
                } else {
                    responseWrapper.setException(new Exception("invalid request，server confused to response"));
                }
                rpcFilter.after(responseWrapper.getResponse());
            } else {
                responseWrapper.setResponse(method.invoke(rpcFilterServerBean.getObject(), requestObjects));
            }

        } catch (Exception e) {
            LOG.error("server handle request error", e);
            responseWrapper.setException(e);
        }
        return responseWrapper;
    }

    @Override
    public void clear() {
        rpcBeanHandlers.clear();
        cacheMethods.clear();
    }

    /**
     * 获取method的唯一键
     *
     * @param instanceName
     * @param methodName
     *
     * @return
     */
    private String getMethodKey(String instanceName, String methodName, String[] argTypes) {
        StringBuilder methodKeyBuilder = new StringBuilder();
        methodKeyBuilder.append(instanceName).append("#");
        methodKeyBuilder.append(methodName).append("$");
        for (String argType : argTypes) {
            methodKeyBuilder.append(argType).append("_");
        }
        return methodKeyBuilder.toString();
    }

    static class SingletonHolder {
        public static RpcTcpServerHandler rpcTcpServerHandler = new RpcTcpServerHandler();
    }

    class RpcFilterServerBean {

        private Object object;

        private RpcInterceptor rpcFilter;

        public RpcFilterServerBean(Object object, RpcInterceptor rpcFilter) {
            super();
            this.object = object;
            this.rpcFilter = rpcFilter;
        }

        /**
         * @return the object
         */
        public Object getObject() {
            return object;
        }

        /**
         * @param object the object to set
         */
        public void setObject(Object object) {
            this.object = object;
        }

        /**
         * @return the rpcFilter
         */
        public RpcInterceptor getRpcFilter() {
            return rpcFilter;
        }

        /**
         * @param rpcFilter the rpcFilter to set
         */
        public void setRpcFilter(RpcInterceptor rpcFilter) {
            this.rpcFilter = rpcFilter;
        }

    }
}
