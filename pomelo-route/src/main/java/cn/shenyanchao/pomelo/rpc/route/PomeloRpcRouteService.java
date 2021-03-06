package cn.shenyanchao.pomelo.rpc.route;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.google.inject.Singleton;

import cn.shenyanchao.pomelo.rpc.core.server.intercepotr.RpcInterceptor;
import cn.shenyanchao.pomelo.rpc.util.ClassPoolUtils;

/**
 * @author shenyanchao
 */

@Singleton
public class PomeloRpcRouteService implements IRpcRouteService {

    private List<RpcRouteInfo> rpcRouteInfos = new ArrayList<>();

    @Override
    public RpcRouteInfo isRouteInfos(String route, String methodType, Map<String, Object> params) throws Exception {
        RpcRouteInfo rpcRoute = null;
        for (RpcRouteInfo rpcRouteInfo : rpcRouteInfos) {
            if (rpcRouteInfo.getRoute().equals(route) && rpcRouteInfo.getHttpType().equals(methodType)) {
                rpcRouteInfo.setParams(params);
                rpcRoute = rpcRouteInfo;
                break;
            }
        }
        return rpcRoute;
    }

    @Override
    public Object methodInvoke(RpcRouteInfo routeInfo) throws Exception {
        Method[] mds = routeInfo.getMethods();
        for (Method method : mds) {
            if (routeInfo.getMethod().equals(method.getName())) {
                Class<?>[] mdTypes = method.getParameterTypes();

                Object[] objs = new Object[mdTypes.length];
                String[] paramNames =
                        ClassPoolUtils.getMethodVariableName(routeInfo.getObjCls().getName(), routeInfo.getMethod());
                for (int j = 0; j < mdTypes.length; j++) {

                    if (mdTypes[j].isAssignableFrom(Map.class)) {//hashmap
                        objs[j] = routeInfo.getParams();
                    } else if (mdTypes[j].isArray()) {//数组
                        Map<String, Object> params = routeInfo.getParams();
                        Object[] object = new Object[params.keySet()
                                .size()];
                        int k = 0;
                        for (String key : params.keySet()) {
                            object[k] = params.get(key);
                            k++;
                        }
                        objs[j] = object;
                    } else if (Collection.class.isAssignableFrom(mdTypes[j])) {//list 类型
                        Map<String, Object> params = routeInfo
                                .getParams();
                        List<Object> result = new LinkedList<Object>();
                        for (String key : params.keySet()) {
                            result.add(params.get(key));
                        }
                        objs[j] = result;
                    } else if (mdTypes[j].isPrimitive()
                            || mdTypes[j].getName().startsWith("java.")) {// java 私有类型
                        Map<String, Object> params = routeInfo.getParams();

                        if (null != paramNames && null != paramNames[j]) {
                            if (params.containsKey(paramNames[j])) {
                                objs[j] = params.get(paramNames[j]);
                            }
                        }

                    } else {// javabean
                        objs[j] = mdTypes[j].newInstance();
                        //TODO
                        String json = JSONObject.toJSONString(routeInfo.getParams());
                        objs[j] = JSONObject.parseObject(json, objs[j].getClass());
                    }
                }

                method.setAccessible(true);
                Object result;
                Object object = routeInfo.getObjCls().newInstance();
                if (null != routeInfo.getRpcInterceptor()) {
                    RpcInterceptor rpcInterceptor = routeInfo.getRpcInterceptor();
                    if (rpcInterceptor.before(method, object, objs)) {
                        result = getResult(method, object, objs);
                    } else {
                        throw new Exception("无效的请求，服务端已经拒绝回应");
                    }
                    rpcInterceptor.after(result);

                } else {

                    result = getResult(method, object, objs);

                }
                return result;
            }
        }
        return null;
    }

    private Object getResult(Method method, Object object, Object... args) throws Exception {
        return method.invoke(object, args);
    }

    @Override
    public void registerRoute(String projectName, Object instance, RpcInterceptor rpcFilter, String httpType,
                              String returnType) {
        Method[] methods = instance.getClass().getDeclaredMethods();
        for (Method method : methods) {
            RpcRouteInfo rpcRouteInfo = new RpcRouteInfo();
            rpcRouteInfo.setObjCls(instance.getClass());
            String simplename = instance.getClass().getSimpleName();
            simplename = simplename.substring(0, 1).toLowerCase() + simplename.substring(1);

            rpcRouteInfo.setHttpType(httpType);
            rpcRouteInfo.setReturnType(returnType);
            rpcRouteInfo.setMethods(methods);
            rpcRouteInfo.setMethod(method.getName());
            rpcRouteInfo.setReturnType(returnType);
            rpcRouteInfo.setRpcInterceptor(rpcFilter);
            String route = projectName + "/" + simplename + "/" + rpcRouteInfo.getMethod();
            rpcRouteInfo.setRoute(route);

            rpcRouteInfos.add(rpcRouteInfo);
        }

    }

    @Override
    public void clear() {
        rpcRouteInfos.clear();
    }
}
