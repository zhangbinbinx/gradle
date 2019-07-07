package com.me.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.SctpChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.Data;
import org.msgpack.MessagePack;
import org.msgpack.MessageTypeException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description:
 * @author: zhangbinbin
 * @create: 2019-07-01 22:51
 **/
@Component(value = "nioRpcServer")
@Data
public class NIORpcServer implements ApplicationContextAware, InitializingBean {
    ExecutorService executorService = Executors.newCachedThreadPool();
    private Map<String,Object> handlerMap = new HashMap<String,Object>();
    private Integer port;

    public NIORpcServer(int port) {
        this.port = port;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        //ServerSocket serverSocket = null;
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //客户端编解码
                            //解码器
                            /** 解析自定义协议 */
                            ch.pipeline().addLast(new Encoder());  //Outbound
                            ch.pipeline().addLast(new Decoder());  //Inbound

                            ch.pipeline().addLast(new TerminalServerHandler());
                           /* //解码器
                            ch.pipeline().addLast(new HttpResponseEncoder());
                            //编码
                            ch.pipeline().addLast(new HttpRequestDecoder());*/
                            //ch.pipeline().addLast(new ServerHandler());
                            /* ch.pipeline().addLast(new HttpResponseEncoder());
                            //编码
                            ch.pipeline().addLast(new HttpRequestDecoder());*/
                            /*ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                            //自定义协议编码器
                            ch.pipeline().addLast(new LengthFieldPrepender(4));
                            //对象参数类型编码器
                            ch.pipeline().addLast("encoder",new ObjectEncoder());
                            //对象参数类型解码器
                            ch.pipeline().addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                            //数据解析处理
                            ch.pipeline().addLast(new ServerHandler());*/
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            System.out.println("服务器启动，绑定端口号为：" + this.port);
            channelFuture.channel().closeFuture().sync();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String,Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if(!serviceBeanMap.isEmpty()){
            for (Object serviceBean : serviceBeanMap.values()) {
                RpcService rpcService = serviceBean.getClass().getAnnotation(RpcService.class);
                String serviceName=rpcService.value().getName();//拿到接口类定义
                String version=rpcService.version(); //拿到版本号
                if(!StringUtils.isEmpty(version)){
                    serviceName+="-"+version;
                }
                handlerMap.put(serviceName,serviceBean);
            }

        }
    }
    public class ServerHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Object result = new Object();
            Channel client = ctx.channel();

            //当客户端建立连接时，需要从自定义协议中获取信息，拿到具体的服务和实参
            //使用反射调用
           /* RpcRequest rpcRequest = (RpcRequest)msg;
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
            ctx.write(result);
            ctx.flush();
            ctx.close();*/
        }
    }
    public class Decoder extends ByteToMessageDecoder {


        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) throws Exception {
            try{
                //先获取可读字节数
                final int length = in.readableBytes();
                final byte[] array = new byte[length];
                String content = new String(array,in.readerIndex(),length);

                //空消息不解析
                if(!(null == content || "".equals(content.trim()))){
                   /* if(!IMP.isIMP(content)){
                        ctx.channel().pipeline().remove(this);
                        return;
                    }*/
                    ctx.channel().pipeline().remove(this);
                   return;
                }

                in.getBytes(in.readerIndex(), array, 0, length);
                MessagePack messagePack = new MessagePack();
                messagePack.register(RpcRequest.class);
                out.add(messagePack.read(array,RpcRequest.class));
                in.clear();
              /*  System.out.println("ByteBuf可读字节数:" + in.readableBytes());
                if (in.readableBytes() >= 8) {
                    out.add(in.readLong());
                }*/
            }catch(MessageTypeException e){
                System.out.println(e.getCause());
                ctx.channel().pipeline().remove(this);
            }
        }
    }
    public class Encoder extends MessageToByteEncoder<RpcRequest>{

        @Override
        protected void encode(ChannelHandlerContext ctx, RpcRequest msg, ByteBuf out) throws Exception {
            out.writeBytes(new MessagePack().write(msg));
           // out.writeBytes(msg.toString().getBytes());
        }
    }
    public class TerminalServerHandler extends SimpleChannelInboundHandler<RpcRequest> {


        @Override
        protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
            if(request instanceof HttpRequest){
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
                ctx.write(result);
                ctx.flush();
                ctx.close();
            }

        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            System.out.println("与客户端断开连接！");
            super.exceptionCaught(ctx, cause);
            ctx.close();
        }
    }
}
