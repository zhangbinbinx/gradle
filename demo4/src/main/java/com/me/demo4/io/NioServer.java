package com.me.demo4.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    private int port = 8090;
    //轮询器
    private Selector selector;
    //缓冲区
    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    public NioServer(int port){
        this.port = port;
        try {
            ServerSocketChannel server = ServerSocketChannel.open();
            //绑定端口
            server.bind(new InetSocketAddress(port));
            //设置非阻塞
            server.configureBlocking(false);
            selector = Selector.open();
            //注册并设置key阻塞
            server.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void listen(){
        System.out.println("listen on " + this.port + "!");

        try {
            while (true) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while(iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    process(key);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void process(SelectionKey key) throws IOException {
        if(key.isAcceptable()){
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            SocketChannel channel = server.accept();
            channel.configureBlocking(false);
            key = channel.register(selector,SelectionKey.OP_READ);
        }else if(key.isReadable()){
            SocketChannel channel = (SocketChannel)key.channel();
            int len = channel.read(buffer);
            if(len > 0){
                //方法作用？
                buffer.flip();
                String content = new String(buffer.array(),0,len);
                key = channel.register(selector,SelectionKey.OP_WRITE);
                key.attach(content);
                System.out.println("读取内容" + content);
            }
        }else if(key.isWritable()){
            SocketChannel channel = (SocketChannel)key.channel();
            String content = (String) key.attachment();
            channel.write(ByteBuffer.wrap(("输出:" + content).getBytes()));
            channel.close();
        }
    }

    public static void main(String[] args) {
        new NioServer(8090).listen();
    }
}
