package com.me.springbootstarter.demo.starter.format;

import java.util.Objects;

/**
 * @description: String格式化
 * @author: zhangbinbin
 * @create: 2019-07-22 22:13
 **/

public class StringFormat implements IFormatProcess {
    @Override
    public <T> String format(T obj) {
        return "StringFormatProcessor:"+ Objects.toString(obj);
    }
}
