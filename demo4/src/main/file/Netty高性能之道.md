## Netty高性能之道

1、为什么都说Netty是高性能的RPC框架？

```html
1 netty 使用了NIO份的方式进行文件传输，提交了CPU的使用率，降低了等待时间
2 netty 零拷贝 使用堆外内存进行socket的读写，降低资源的使用率
3 无锁化的串行设计
4 使用高效的Reactor模型
5 高效的并发编程
```



2、服务端的Socket在哪里开始初始化？

```html
服务端初始化步骤：
1 新建bossgroup
2 新建workgroup
  新建分组时，会初始化执行器和子线程的信息
3 新建serverbootstrap并绑定分组及设置属性
   初始化使用的channel对应的类及其他属性
4 绑定端口并同步
  bind时，会初始化注册channel到分组中
  bind 注册时会执行new channel(),根据初始化的channel初始化对应的socket
5 关闭
```



3、服务端的Socket在哪里开始accept链接？

```html
bind完成后，会执行sync方法，sync方法会执行 await()方法，使当前线程阻塞
```

