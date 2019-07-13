package com.me;

import com.me.zk.ZKConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.GetDataBuilder;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

import java.io.IOException;

/**
 * @description: zk测试
 * @author: zhangbinbin
 * @create: 2019-07-13 15:46
 **/

public class ZKTest {
    static final String connectionStr = "192.168.201.135:2181";
    @Test
    public void test() throws Exception {

        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString(connectionStr).sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000,3)).build();
        curatorFramework.start();
        //Thread.sleep(5000);
        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/node2");
        //  GetDataBuilder data = curatorFramework.getData();
        System.out.println("");
       /* ZooKeeper zk = new ZooKeeper("192.168.201.135:2555", 5000, new Watcher() {

            @Override
            public void process(WatchedEvent event) {
                System.out.println("数据发生改变");
            }
        });*/
    }
}
