package com.chart.protocol;

import lombok.Data;

/**
 * @description: 消息
 * @author: zhangbinbin
 * @create: 2019-06-26 21:12
 **/
@Data
@Message
public class IMMessage {
    private String addr;
    private String cmd;
    private String time;
    private String online;
    private String sender;
    private String receiver;
    private String content;
    private String termial;
    public IMMessage(){}

}
