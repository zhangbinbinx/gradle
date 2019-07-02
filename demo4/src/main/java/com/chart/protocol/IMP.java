package com.chart.protocol;

/**
 * @description: 自定义协议
 * @author: zhangbinbin
 * @create: 2019-06-26 21:06
 **/

public enum IMP {
    SYSTEM("SYSTEM"),
    LOGIN("LOGIN"),
    LOGOUT("LOGOUT");
    CHAT("CHAT");
    FLOWER("FLOWER");
    private String name;
    IMP(String name){
        this.name = name;
    }
    public static boolean isIMP(String content){
        return content.matches("^\\[(SYSTEM|LOGIN|LOGOUT|CHAT|FLOWER)]");
    }
    public String getName(){
        return this.name;
    }
    public String toString(){
        return this.toString();
    }
}
