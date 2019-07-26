package com.me.springbootstarter.demo.starter;

import com.me.springbootstarter.demo.starter.autoconfiguration.MyProperties;
import com.me.springbootstarter.demo.starter.format.IFormatProcess;

/**
 * @description: 模板类
 * @author: zhangbinbin
 * @create: 2019-07-22 22:08
 **/

public class HelloFormatTemplate {
    private IFormatProcess formatProcess;
    private MyProperties properties;

    public HelloFormatTemplate(IFormatProcess formatProcess, MyProperties properties) {
        this.formatProcess = formatProcess;
        this.properties = properties;
    }
    public <T> String doFormat(T obj){
        StringBuilder sb = new StringBuilder();
        sb.append("begin:Execute format").append("<br/>");
        sb.append("HelloProperties:").append(formatProcess.format(properties.getInfo())).append("<br/>");
        sb.append("Obj format result:").append(formatProcess.format(obj)).append("<br/>");
        return sb.toString();
    }
}
