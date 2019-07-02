package com.me.server;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: rpc请求封装
 * @author: zhangbinbin
 * @create: 2019-07-01 21:15
 **/
@Data
public class RpcRequest implements Serializable {
    private String className;
    private String methodName;
    private Object[] params;
    private String version;

}
