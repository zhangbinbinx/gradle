package com.me.server;

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

public class ProcessHandler implements Runnable {
    private Socket socket;
    private Map<String,Object> hadlerMap;
    public ProcessHandler(Socket socket,Map<String,Object> hadlerMap) {
        this.socket = socket;
        this.hadlerMap = hadlerMap;
    }

    @Override
    public void run() {
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try{
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            Object result = invoke(rpcRequest);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(result);
            objectOutputStream.flush();
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
        }
    }

    private Object invoke(RpcRequest rpcRequest) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String serviceName = rpcRequest.getClassName();
        String version = rpcRequest.getVersion();
        if(!StringUtils.isEmpty(version)){
            serviceName += "-" + version;
        }
        Object service = hadlerMap.get(serviceName);
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
        Object result = method.invoke(service,args);
        return result;
    }
}
