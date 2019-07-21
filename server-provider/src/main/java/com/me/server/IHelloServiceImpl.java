package com.me.server;

/**
 * @description: Hello实现类
 * @author: zhangbinbin
 * @create: 2019-07-01 22:05
 **/
@RpcService(value = IHelloService.class,version = "1.0")
public class IHelloServiceImpl implements IHelloService {
    @Override
    public String sayHello(String content) {
        System.out.println("执行接口方法！");
        return "Hello:" + content;

    }

   /* @Override
    public String saveUser(User user) {
        System.out.println("保存用户信息成功！当前用户信息为\n" + user.toString() + "!");
        return "SUCCESS";
    }*/


}
