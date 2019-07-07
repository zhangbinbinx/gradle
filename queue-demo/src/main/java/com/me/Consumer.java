package com.me;

/**
 * @description: 消费者
 * @author: zhangbinbin
 * @create: 2019-07-07 12:56
 **/

public class Consumer implements Runnable {
    public  void salltor() throws InterruptedException {
        System.out.println("供应商准备去取牛奶");
        for (int i = 0; i <35; i++) {
            Milk milk = null;
            try {
                milk = Repository.getMilk();
                System.out.println(milk.toString());
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public void run() {
        System.out.println("供应商准备去取牛奶");
        for (int i = 0; i <35; i++) {
            Milk milk = null;
            try {
                milk = Repository.getMilk();
                System.out.println(milk.toString());
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }
}
