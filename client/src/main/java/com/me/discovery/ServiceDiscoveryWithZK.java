package com.me.discovery;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description: zk服务发现
 * @author: zhangbinbin
 * @create: 2019-07-13 16:48
 **/

public class ServiceDiscoveryWithZK implements IServiceDiscovery {
    CuratorFramework curatorFramework = null;
    List<String> discoveryList = new ArrayList<String>();
    {
        curatorFramework = CuratorFrameworkFactory.builder().connectString(ZKConfig.CONNECTION_STR).sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000,3)).build();
        curatorFramework.start();
    }
    @Override
    public String discovery(String serviceName) {
        String path = "/" + serviceName;
        if(discoveryList.isEmpty()){
            try {
                discoveryList = curatorFramework.getChildren().forPath(path);
                registryWatch(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //String result = discoveryList.get(0)
        return discoveryList.get(0);
    }

    private void registryWatch(final String path) throws Exception {
        PathChildrenCache nodeCache = new PathChildrenCache(curatorFramework,path,true);
        PathChildrenCacheListener cacheListener = (curatorFramework1, pathChildrenCacheEvent) ->{
            System.out.println("客户端收到节点变更的事件");
            discoveryList=curatorFramework1.getChildren().forPath(path);// 再次更新本地的缓存地址
        };
        nodeCache.getListenable().addListener(cacheListener);
        nodeCache.start();
    }

}
