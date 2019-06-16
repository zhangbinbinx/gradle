package com.me.demo4.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class BioServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8090);
        System.out.println("启动服务器成功！");

        while(true){
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuffer sb = new StringBuffer();
            String str = null;
            while ((str = reader.readLine()) != null)
            {
                sb.append(str).append("\n");
            }
            System.out.println("服务器收到信息：" + new String(sb.toString()));
            sb=null;
            //inputStreamReader.close();
            //inputStreamReader = null;
            //inputStream.close();
        }
    }

}
