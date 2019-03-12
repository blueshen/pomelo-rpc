package cn.shenyanchao.pomelo.rpc.demo.service.impl;

import org.springframework.stereotype.Service;

import cn.shenyanchao.pomelo.rpc.core.annotation.PomeloRpc;
import cn.shenyanchao.pomelo.rpc.demo.entity.RpcUser;
import cn.shenyanchao.pomelo.rpc.demo.service.IHelloService;

/**
 * @author shenyanchao
 */

@PomeloRpc(group = "hello", name = "helloService")
@Service
public class HelloService implements IHelloService {

    @Override
    public String sayHi(String name) {
        return "hi, " + name;
    }

    @Override
    public RpcUser getUser(String name, String age) {
        RpcUser rpcUser = new RpcUser();
        rpcUser.setName(name);
        rpcUser.setAge(age);
        return rpcUser;
    }

    @Override
    public String sayHiToUser(RpcUser rpcUser) {
        return "hi, your name is " + rpcUser.getName() + ", age is " + rpcUser.getAge();
    }
}