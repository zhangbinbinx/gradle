package com.me;

import com.me.server.RpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @description: 远程数据传输
 * @author: zhangbinbin
 * @create: 2019-07-02 20:49
 **/

public class RpcNetTransport {
    private int port;
    private String host;

    public RpcNetTransport(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public Object send(RpcRequest rpcRequest){
        //Socket socket = null;
        Bootstrap boostrap = null;
        Object result  = null;
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try{
            boostrap = new Bootstrap();
            ChannelFuture channel = boostrap.connect(host, port);
            channel.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    future.
                }
            });
            /*socket = new Socket(host,port);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();
            objectInputStream = new ObjectInputStream(socket.getInputStream());*/
            result = objectInputStream.readObject();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(null != objectInputStream){
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != objectOutputStream){
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != socket){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
    }
}
