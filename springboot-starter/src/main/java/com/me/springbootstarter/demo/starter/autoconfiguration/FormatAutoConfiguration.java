package com.me.springbootstarter.demo.starter.autoconfiguration;

import com.me.springbootstarter.demo.starter.format.IFormatProcess;
import com.me.springbootstarter.demo.starter.format.JsonFormatProcess;
import com.me.springbootstarter.demo.starter.format.StringFormat;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @description: 自动装配
 * @author: zhangbinbin
 * @create: 2019-07-22 22:23
 **/
@Configuration
public class FormatAutoConfiguration {
    @ConditionalOnMissingClass("com.alibaba.fastjson.JSON")
    @Bean
    @Primary
    public IFormatProcess stringFormat(){
        return new StringFormat();
    }
    @ConditionalOnClass(name="com.alibaba.fastjson.JSON")
    @Bean
    public IFormatProcess jsonFormat(){
        return new JsonFormatProcess();
    }
}
