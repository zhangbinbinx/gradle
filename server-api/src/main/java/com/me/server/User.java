package com.me.server;

import lombok.Data;

/**
 * @description:
 * @author: zhangbinbin
 * @create: 2019-07-01 21:06
 **/
@Data
public class User {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
