package com.me.tomcat;


import com.me.tomcat.http.Request;
import com.me.tomcat.http.Response;

import com.me.tomcat.http.Servlet;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Tomcat {
    //端口
    private int port = 8080;
    private Map<String, Servlet> servletMapping = new HashMap<String,Servlet>();
    private String resourceFileName = "web.properties";
    private Properties webxml = new Properties();
    public void init(){
        try{
            String resource = this.getClass().getClassLoader().getResource(resourceFileName).getPath();
            FileInputStream fis = new FileInputStream(resource);
            webxml.load(fis);
            for (Object key : webxml.keySet()) {
                if(null != key){
                    String currentKey = key.toString();
                    if(currentKey.endsWith(".url")){
                        String servletName = currentKey.replaceAll("\\.url$","");
                        String url = webxml.getProperty(currentKey);
                        String className = webxml.getProperty(servletName + ".className");
                        Servlet servlet = (Servlet)Class.forName(className).newInstance();
                        servletMapping.put(url,servlet);
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        //severSocket = new ServerSocket(port);


    }

    public void start(){
        init();
        //boss线程
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        //worker线程
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap server = new ServerBootstrap();
            //设置调度及工作线程
            server.group(boosGroup,workerGroup)
                    //设置通道
                    .channel(NioServerSocketChannel.class)
                    //客户端回调
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel client) throws Exception {
                           //客户端编解码
                            //解码器
                            client.pipeline().addLast(new HttpResponseEncoder());
                            //编码
                            client.pipeline().addLast(new HttpRequestDecoder());
                            client.pipeline().addLast(new TomcatHandler());
                        }
                    })//最大线程数
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);
            ChannelFuture f = server.bind(port).sync();
            System.out.println("服务器启动成功！当前端口为" + port + "!");
            f.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    public class TomcatHandler extends ChannelInboundHandlerAdapter{
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if(msg instanceof HttpRequest){
                HttpRequest req = (HttpRequest)msg;
                Request request = new Request(ctx,req);
                Response response = new Response(ctx,req);
                String url = request.getUrl();
                if(servletMapping.containsKey(url)){
                    servletMapping.get(url).service(request,response);
                }else{
                    response.write("404-not found");
                }
            }
        }
    }

    public static void main(String[] args) {
        new Tomcat().start();
    }
}
