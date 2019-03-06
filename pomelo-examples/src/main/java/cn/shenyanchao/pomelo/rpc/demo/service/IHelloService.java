package cn.shenyanchao.pomelo.rpc.demo.service;

import cn.shenyanchao.pomelo.rpc.demo.entity.RpcUser;

/**
 * @author shenyanchao
 */
public interface IHelloService {

    String sayHi(String name);

    RpcUser getUser(String name, String age);

    String sayHiToUser(RpcUser rpcUser);
}
