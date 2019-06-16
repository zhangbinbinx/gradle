package com.me.demo4.io;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class BioClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("localhost",8090);
        System.out.println("启动客户端成功！");
        OutputStream outputStream = socket.getOutputStream();
        String str = "first send";
        outputStream.write(str.getBytes("utf-8"));
        outputStream.flush();
        //outputStream.close();

        Thread.sleep(1000);
        outputStream = socket.getOutputStream();
        str = "second send";
        outputStream.write(str.getBytes("utf-8"));
        outputStream.flush();
        outputStream.close();

    }
}
