package com.me.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @description: zk服务注册中心
 * @author: zhangbinbin
 * @create: 2019-07-11 22:21
 **/

public class ZKServiceRegistry implements IServiceRegistry {
    CuratorFramework curatorFramework = null;
    {
        curatorFramework = CuratorFrameworkFactory.builder().connectString(ZKConfig.CONNECTION_STR).sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000,3)).build();
        curatorFramework.start();
    }
    @Override
    public void registry(String serviceName, String serviceAddress) {
        String servicePath = "/" + serviceName;
        try{
            if(null == curatorFramework.checkExists().forPath(servicePath)){
                curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(servicePath);
            }
            String addressPath = servicePath + "/" + serviceAddress;
            curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath(addressPath);
            System.out.println("服务注册成功！");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
