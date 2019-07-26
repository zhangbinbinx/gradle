package com.me.springbootstarter.demo.starter.format;

import com.alibaba.fastjson.JSON;

/**
 * @description: json格式化类
 * @author: zhangbinbin
 * @create: 2019-07-22 22:11
 **/

public class JsonFormatProcess implements IFormatProcess {
    @Override
    public <T> String format(T obj) {
        return "JsonFormatProcessor:"+ JSON.toJSONString(obj);
    }
}
