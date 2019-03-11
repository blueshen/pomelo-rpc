package cn.shenyanchao.pomelo.rpc.tcp.netty4.server.handler;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import cn.shenyanchao.pomelo.rpc.core.message.PomeloRequestMessage;
import cn.shenyanchao.pomelo.rpc.core.message.PomeloResponseMessage;
import cn.shenyanchao.pomelo.rpc.core.server.handler.RpcServerHandler;
import cn.shenyanchao.pomelo.rpc.core.server.intercepotr.RpcInterceptor;
import cn.shenyanchao.pomelo.rpc.serialize.PomeloSerializer;

/**
 * 服务端处理请求类
 *
 * @author shenyanchao
 */

@Singleton
public class RpcTcpServerHandler implements RpcServerHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RpcTcpServerHandler.class);

    /**
     * 保存处理bean实例
     */
    private Map<String, RpcFilterServerBean> rpcBeanHandlers = new ConcurrentHashMap<>(128);

    private Map<String, Method> cacheMethods = new ConcurrentHashMap<>(256);

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

    public PomeloResponseMessage handleRequest(PomeloRequestMessage request, PomeloSerializer serializer,
                                               byte protocolType) {
        PomeloResponseMessage responseWrapper = new PomeloResponseMessage(request.getId(), serializer, protocolType);
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
                        requestObjects[i] = request.getSerializer().getSerialization()
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
            if (null != rpcFilterServerBean.getRpcInterceptor()) {
                // filter 过滤
                RpcInterceptor rpcFilter = rpcFilterServerBean.getRpcInterceptor();
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

    class RpcFilterServerBean {

        private Object object;

        private RpcInterceptor rpcInterceptor;

        public RpcFilterServerBean(Object object, RpcInterceptor rpcInterceptor) {
            super();
            this.object = object;
            this.rpcInterceptor = rpcInterceptor;
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

        public RpcInterceptor getRpcInterceptor() {
            return rpcInterceptor;
        }

        public void setRpcInterceptor(RpcInterceptor rpcInterceptor) {
            this.rpcInterceptor = rpcInterceptor;
        }
    }
}
