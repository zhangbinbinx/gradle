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
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
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

public class NIORpcNetTransport extends SimpleChannelInboundHandler<Object>{
    private int port;
    private String host;
    private Object result;
    public NIORpcNetTransport(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public Object send(RpcRequest rpcRequest){
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap boostrap = null;
        try{
            boostrap = new Bootstrap();
            boostrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null))).
                                    addLast(new ObjectEncoder()).
                                    addLast(NIORpcNetTransport.this);
                        }
                    }).option(ChannelOption.TCP_NODELAY,true);
            ChannelFuture channelFuture = boostrap.connect(host, port).sync();
            channelFuture.channel().writeAndFlush(rpcRequest).sync();
            if(null != rpcRequest){
                channelFuture.channel().closeFuture().sync();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
            return result;
        }


    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.result = msg;
    }
}

