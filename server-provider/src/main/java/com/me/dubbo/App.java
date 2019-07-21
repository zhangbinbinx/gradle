package com.me.dubbo;

import org.apache.dubbo.container.Main;


import java.io.IOException;

/**
 * @description: 启动类
 * @author: zhangbinbin
 * @create: 2019-07-01 22:39
 **/

public class App {
    public static void main(String[] args) throws IOException {


        Main.main(args);
        System.in.read(); // 按任意键退出
    }
}
