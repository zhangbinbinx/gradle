package com.me;

import com.me.server.IHelloService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @description: 启动类
 * @author: zhangbinbin
 * @create: 2019-07-01 22:39
 **/

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        RpcProxyClient proxyClient = context.getBean(RpcProxyClient.class);
        IHelloService helloService = proxyClient.clientProxy(IHelloService.class,8080,"localhost");
        String result = helloService.sayHello("mic");
        System.out.println(result);
    }
}
