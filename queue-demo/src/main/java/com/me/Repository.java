package com.me;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @description: 仓库
 * @author: zhangbinbin
 * @create: 2019-07-07 12:57
 **/

public  class Repository {
    public static int MAX_SIZE = 20;
    public static ArrayBlockingQueue<Milk> ARRAY_BLOCKING_QUEUE = new ArrayBlockingQueue<Milk>(MAX_SIZE);
    public static void putMilk(Milk milk) throws InterruptedException {

        printCurrentSize();
        ARRAY_BLOCKING_QUEUE.put(milk);
    }
    public static Milk getMilk() throws InterruptedException {
        printCurrentSize();
        return ARRAY_BLOCKING_QUEUE.take();
    }
    private static void printCurrentSize(){
        System.out.println("当前库存为：" + ARRAY_BLOCKING_QUEUE.size());
    }

}
