package com.me;

import com.me.discovery.ServiceDiscoveryWithZK;
import com.me.server.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @description: 远程调用接口
 * @author: zhangbinbin
 * @create: 2019-07-02 20:45
 **/

public class RemoteInvocationHandler implements InvocationHandler {
    private int port;
    private String host;

    public RemoteInvocationHandler(int port, String host) {
        this.port = port;
        this.host = host;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("调用InvocationHandler接口！");
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParams(args);
        rpcRequest.setVersion("1.0");
        //NIORpcNetTransport transport = new NIORpcNetTransport(port,host);
       //RpcNetTransport transport = new RpcNetTransport(port,host);
        String serviceAddress = new ServiceDiscoveryWithZK().discovery(method.getDeclaringClass().getName() + "-1.0");
        NIORpcNetTransport transport = new NIORpcNetTransport(serviceAddress);
        Object result = transport.send(rpcRequest);
        return result;
    }
}
