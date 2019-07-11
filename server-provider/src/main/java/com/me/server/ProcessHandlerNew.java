package com.me.server;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpRequest;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

/**
 * @description:
 * @author: zhangbinbin
 * @create: 2019-07-01 22:58
 **/

public class ProcessHandlerNew extends SimpleChannelInboundHandler<RpcRequest> {
    private Map<String,Object> handlerMap;
    public ProcessHandlerNew(Map<String,Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        Object result = new Object();
        //当客户端建立连接时，需要从自定义协议中获取信息，拿到具体的服务和实参
        //使用反射调用
        RpcRequest rpcRequest = request;
        String serviceName = rpcRequest.getClassName();
        String version = rpcRequest.getVersion();
        if(!StringUtils.isEmpty(version)){
            serviceName += "-" + version;
        }
        Object service = handlerMap.get(serviceName);
        if(null == service){
            throw new RuntimeException("service is not fund:" + serviceName);
        }
        Object []args = rpcRequest.getParams();
        Method method = null;
        Class clazz = Class.forName(rpcRequest.getClassName());
        if(null != args && args.length > 0){
            Class<?> []types = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                types[i] = args[i].getClass();
            }
            method = clazz.getMethod(rpcRequest.getMethodName(),types);
        }else{
            method = clazz.getMethod(rpcRequest.getMethodName());
        }
        result = method.invoke(service,args);
        ctx.writeAndFlush(result).addListener(ChannelFutureListener.CLOSE);
       /* ctx.write(result);
        ctx.flush();
        ctx.close();*/
    }


}
