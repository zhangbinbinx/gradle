package com.me;

/**
 * @description: 测试类
 * @author: zhangbinbin
 * @create: 2019-07-07 13:43
 **/

public class Test {
    public static void salltor(){
        System.out.println("开始准备取货物");
        new Thread(()->{
            try {
                Consumer consumer = new Consumer();
                consumer.salltor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    public static void creator(){
        System.out.println("开始准备生产货物");
        new Thread(()->{
            try {
                Creator creator = new Creator();
                creator.creator();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    public static void main(String[] args) throws InterruptedException {
        salltor();
        creator();
       // Thread.sleep(5000000);
    }
}

