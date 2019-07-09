## zookeeper部署记录

### 1.开启以下服务

```html
VMware Authonrization Service：用于启动和访问虚拟机的授权和身份验证服务

VMware DHCP Service: IP自动分配协议——它不启动 虚拟机不能上网

VMware NAT Service: 虚拟地址转换协议——它不启动 虚拟机不能上网

VMware USB Arbitration Service:U盘接口服务——它不启动 虚拟机无法识别usb

VMware Workstation Server:用于虚拟机的注册和管理远程访问服务！

VitrualDisk  虚拟机硬盘

```

![1562594571839](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\1562594571839.png)

### 2.复制下载的zk  tar包文件到虚拟机

### 3.复制软件到/usr/local/src目录下

###4.解压下载的tar包   tar  -zxvf  zookeeper-3.5.5-bin.tar.gz

### 5.cd到安装目录下的bin目录下，启动zkServer.sh，报错如下

![1562595335168](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\1562595335168.png)

### 6.添加zoo.cfg文件，复制并查看文件内容

![1562595488810](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\1562595488810.png)

### 7.重新启动zk,启动成功

### 8.查看当前linux上的ip

### 9.使用zooview连接并查看节点

![1562595843845](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\1562595843845.png)

### 10.启动zk客户端 ./zkCli.sh

![1562596469775](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\1562596469775.png)

### 11.在虚拟机上添加节点并观察zooView是否同步展示

![1562596611740](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\1562596611740.png)

### 12.单机节点搭建完成

### 13.停止当前客户端及服务端

```html
ctrl + z 退出客户端
./zkServer.sh stop 退出服务端
```

### 14.配置另外两个虚拟机，用于做集群部署

### 15.配置ip及端口，数据文件地址

```html
erver.1=192.168.201.135:2555:3555
server.2=192.168.201.137:2555:3555
server.3=192.168.201.137:2555:3555
dataDir=/home/zookeeper/data 
dataLogDir=/home/zookeeper/logs
```

### 16.启动三个服务，并查看服务状态

![1562682230501](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\1562682230501.png)

### 17.查看安装目录下的logs下的out文件

![1562682676394](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\1562682676394.png)

### 18.查看防火墙状态

![1562682868884](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\1562682868884.png)

### 19.关闭防火墙（需要三个全部关闭）

![1562683047593](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\1562683047593.png)

### 20.配置完成

![1562683437947](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\1562683437947.png)

![1562683481414](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\1562683481414.png)

