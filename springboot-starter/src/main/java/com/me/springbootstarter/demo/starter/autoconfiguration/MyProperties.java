package com.me.springbootstarter.demo.starter.autoconfiguration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @description: 属性配置
 * @author: zhangbinbin
 * @create: 2019-07-22 22:15
 **/
@Data
@ConfigurationProperties(prefix = MyProperties.MY_PREV)
public class MyProperties {
    public static final String MY_PREV = "com.me.springbootstarter";
    private Map<String,Object> info;

    public Map<String, Object> getInfo() {
        return info;
    }

    public void setInfo(Map<String, Object> info) {
        this.info = info;
    }
}
