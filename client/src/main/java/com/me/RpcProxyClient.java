package com.me;

import java.lang.reflect.Proxy;

/**
 * @description: rpc代理类
 * @author: zhangbinbin
 * @create: 2019-07-02 20:45
 **/

public class RpcProxyClient {
    public <T>T clientProxy(final Class<T> interfaceCls,final int port,final String host){
        return (T)Proxy.newProxyInstance(interfaceCls.getClassLoader(),new Class<?>[]{interfaceCls},new RemoteInvocationHandler(port,host));
    }
}
