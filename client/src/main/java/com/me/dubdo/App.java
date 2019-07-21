package com.me.dubdo;

import com.me.RpcProxyClient;
import com.me.SpringConfig;
import com.me.server.IHelloService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description: 启动类
 * @author: zhangbinbin
 * @create: 2019-07-01 22:39
 **/

public class App {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"application.xml"});
        context.start();
        IHelloService helloService = (IHelloService)context.getBean("helloService"); // 获取远程服务代理
        String hello = helloService.sayHello("mic"); // 执行远程方法
        System.out.println( hello ); // 显示调用结果
    }
}
