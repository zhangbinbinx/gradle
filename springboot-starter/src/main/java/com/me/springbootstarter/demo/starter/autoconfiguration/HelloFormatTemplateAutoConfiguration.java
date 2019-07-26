package com.me.springbootstarter.demo.starter.autoconfiguration;

import com.me.springbootstarter.demo.starter.HelloFormatTemplate;
import com.me.springbootstarter.demo.starter.format.IFormatProcess;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @description: 自动装配
 * @author: zhangbinbin
 * @create: 2019-07-22 22:27
 **/
@Import(FormatAutoConfiguration.class)
@EnableConfigurationProperties(MyProperties.class)
@Configuration
public class HelloFormatTemplateAutoConfiguration {
    @Bean
    public HelloFormatTemplate helloFormatTemplate(MyProperties properties, IFormatProcess formatProcess){
        return new HelloFormatTemplate(formatProcess,properties);
    }
}
