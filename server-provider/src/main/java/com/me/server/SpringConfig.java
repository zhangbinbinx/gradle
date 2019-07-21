package com.me.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @description: spring配置
 * @author: zhangbinbin
 * @create: 2019-07-01 22:48
 **/
/*@Configuration
@ComponentScan("com.me.server")*/
public class SpringConfig {
    /*@Bean(name="rpcServer")
    public RpcServer rpcServer(){
        return new RpcServer(8080);
    }*/
//@Bean(name="nioRpcServer")
    public NIORpcServer nioRpcServer(){
        return new NIORpcServer(8080);
    }
}
