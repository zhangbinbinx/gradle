package com.me.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description: zk实现分布式锁
 * @author: zhangbinbin
 * @create: 2019-07-15 21:37
 **/

public class ZKLock {
    static CuratorFramework curatorFramework = null;
    static Map<String,Thread> threadMap = new ConcurrentHashMap<String,Thread>();

    {
        curatorFramework = CuratorFrameworkFactory.builder().connectString(ZKConfig.CONNECTION_STR).sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
        curatorFramework.start();
    }

    public void createLockDir(String serviceName) throws Exception {
        //锁锁的是对象
        //第一步 创建加锁目录
        //第二步 把请求添加到加锁目录下
        //第三步 把加锁目录排序并把第一个持有锁
        //第四步 执行第一个持有锁的业务逻辑
        //第五步 释放锁并通知第二个
        String lockNode = "/lock";
        if (null == curatorFramework.checkExists().forPath(lockNode)) {
            curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(lockNode);
        }
        String curPath = curatorFramework.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(lockNode + "/" + serviceName);
        //获取所有节点并排序
        threadMap.put(curPath,Thread.currentThread());
        //判断当前节点是否和最小的名称一致
        getLock(lockNode, curPath,false);
    }

    private void getLock(String lockNode, String curPath,boolean flag) throws Exception {
        if(flag){
            curPath = curPath.substring(lockNode.length() + 1);
        }
        while (!flag) {
            List<String> lockList = curatorFramework.getChildren().forPath(lockNode);
            lockList.sort(new Comparator<String>() {
                @Override
                public int compare(String fir, String sec) {
                    return fir.compareTo(sec);
                }
            });
            if (!lockList.isEmpty()) {
                System.out.println("开始判断获取锁的节点，当前线程名称为" + Thread.currentThread().getName());
                String currentNode = lockList.get(0);
                if (curPath.equals(currentNode)) {

                    //删除节点
                    System.out.println("当前节点数共有" + lockList.size() + "个数据在等待");
                    for (int i = 0; i < lockList.size(); i++) {
                        System.out.println(lockList.get(i));

                    }
                    doService();
                    //Thread.sleep(1000);
                    curatorFramework.delete().forPath(lockNode + "/" + currentNode);
                    //通知第二个线程

                    break;

                }else{
                    Thread.sleep(3000);
                }
            }
        }

    }

    private void doService() {
        System.out.println("当前线程正在执行业务操作，线程名称为:" + Thread.currentThread().getName());
        System.out.println(".........");
    }

    public static void main(String[] args) {
        try {
            for (int i = 0; i < 10; i++) {
                new Thread(() -> {
                    try {
                        new ZKLock().createLockDir("aaa");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public class MyInterProcessMutex {
        private CuratorFramework curatorFramework;
        ReentrantLock lock;

        private void actuire() {
            lock.lock();
        }

        private void release() {
            lock.unlock();
        }
    }
}
