package com.me;

/**
 * @description: 生产者
 * @author: zhangbinbin
 * @create: 2019-07-07 12:55
 **/

public class Creator implements Runnable{
    public  void creator() throws InterruptedException {
        System.out.println("准备开始生产");
        for (int i = 0; i < 50; i++) {
            Milk milk = new Milk();
            milk.setWeight(i + "");
            Repository.putMilk(milk);
            Thread.sleep(200);
        }
    }

    @Override
    public void run() {
        System.out.println("准备开始生产");
        for (int i = 0; i < 50; i++) {
            Milk milk = new Milk();
            milk.setWeight(i + "");
            try {
                Repository.putMilk(milk);
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
