package com.me;

import com.me.server.RpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @description: nio客户端
 * @author: zhangbinbin
 * @create: 2019-07-05 22:45
 **/

public class NIORpcNetTransport {
    private int port;
    private String host;

    public NIORpcNetTransport(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public Object send(RpcRequest rpcRequest){
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap boostrap = null;
        final Object[] result = {};
        try{
            boostrap = new Bootstrap();
            boostrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new SimpleChannelInboundHandler<Socket>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, Socket msg) throws Exception {
                                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(msg.getOutputStream());
                                    objectOutputStream.writeObject(rpcRequest);
                                    objectOutputStream.flush();
                                    ObjectInputStream objectInputStream = new ObjectInputStream(msg.getInputStream());
                                    result[0] = objectInputStream.readObject();
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
                                }
                            });
                        }
                    });
            ChannelFuture channelFuture = boostrap.connect(host, port).sync();
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
            return result[0];
        }


    }
}

