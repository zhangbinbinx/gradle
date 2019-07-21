package com.me;

import org.springframework.context.annotation.Bean;

/**
 * @description: spring配置
 * @author: zhangbinbin
 * @create: 2019-07-02 20:42
 **/

public class SpringConfig {
   // @Bean(name="rpcProxyClient")
    public RpcProxyClient proxyClient(){
        return new RpcProxyClient();
    }
}
