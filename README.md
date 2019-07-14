# 云课堂项目

## 整体架构图

![](http://img.shaking.top/project1.png)

## 应用技术

1. 监控SpringBoot Admin

2. rpc框架dubbo+spring cloud

3. 日志ELK

4. 分布式调度中心nacos

5. 监控中心sentinel 1.6.1

   TODO

## 安装环境

​	部分是docker镜像部分是jar包运行

​	**mysql 5.7**

```shell
# mysql主从容器
docker run --name mysql-master --privileged=true -v /home/mysql/master-data:/var/lib/mysql -p 3307:3306 -e MYSQL_ROOT_PASSWORD=Yuanyulou789! -d xiaochunping/mysql-master
docker run --name mysql-slave --privileged=true -v /home/mysql/slave-data:/var/lib/mysql -p 3308:3306 --link mysql-master:master -e MYSQL_ROOT_PASSWORD=Yuanyulou789 -d xiaochunping/mysql-slave
# 进入主容器
docker exec -it mysql-master /bin/bash
mysql -uroot -pYuanyulou789!
# 创建同步账号密码, *.*代表所有的数据库
grant replication slave on *.* to 'test'@'%' identified by '123456';
flush privileges;
# 查看主的状态
show master status;
# 进入savle开启数据同步
docker exec -it mysql-slave /bin/bash
mysql -uroot -pYuanyulou789!
change master to master_host='master', master_user='test', master_password='123456',
master_port=3306, master_log_file='mysql-bin.000001', master_log_pos=589, master_connect_retry=30;
start slave;
# 如果失败
reset slave;
# 查看从的状态
show slave status\G;
```

> 查看主从关联是否建立成功：
>
> 1. 在主库随便建立一个数据库去从库看是否有同步
> 2. 分别进入主库和从库查看各自的状态；当主库的File和Position和从库的一样并且从库的Slave_IO_Running, Slave_SQL_Running为Yes时，代表成功
>
> ![](http://img.shaking.top/project4.png)
>
> ![](http://img.shaking.top/project5.png)

​	mysql遇到一个问题，本地linux环境有一个mysql，docker上有两个mysql，分别是3306,3307,3308；docker映射的是3306端口，无法本地连接到docker的其他两个数据库

​	使用mysql -uroot -p -P3307 -hlocalhost只会连接到本地的3306数据库，无法连接到docker的数据库。

> 问题原因是创建镜像的时候端口映射错误docker run --name mysql-master --privileged=true -v /home/mysql/master-data:/var/lib/mysql -p 3307:3306 -e MYSQL_ROOT_PASSWORD=Yuanyulou789! -d xiaochunping/mysql-master，其中-p 3307:3306分别是docker的3307端口映射到宿主机的3306端口，原来的3307:3307改成当前命令就好了。

​	**redis 4.x**

```shell
docker run -d --name redis -p 6379:6379 redis redis-server --requirepass "redis123456" --appendonly yes
```

​	**sentinel** 1.6.1

​	下面是shell脚本，任意命名为xxx.sh，存到linux下，chmod + x xxx.sh赋予执行权限，**记得修改jar包位置**和端口号；

```shell
#启动
#注意：启动 Sentinel 控制台需要 JDK 版本为 1.8 及以上版本。

#使用如下命令启动控制台：
#java -Dserver.port=8188 -Dcsp.sentinel.log.dir=./sentinel.log -Dcsp.sentinel.dashboard.server=localhost:8188 -Dproject.name=sentinel-dashboard -Djava.security.egd=file:/yuanyl/sentinel/ -jar /yuanyl/sentinel/sentinel-dashboard-1.6.1.jar

JAVA_OPT="${JAVA_OPT} -Dproject.name=sentinel-dashboard "
JAVA_OPT="${JAVA_OPT} -Dcsp.sentinel.log.dir=./logs "

JAVA_OPT="${JAVA_OPT} -Dserver.port=8188 "
JAVA_OPT="${JAVA_OPT} -Dlogging.file=./logs "
JAVA_OPT="${JAVA_OPT} -Djava.security.egd=file:/dev/./urandom "
JAVA_OPT="${JAVA_OPT} -Dcsp.sentinel.dashboard.server=localhost:8188 "

echo "JAVA_OPTS============"
echo $JAVA_OPT

sudo mkdir -p ./logs

# 后台启动
nohup java $JAVA_OPT -jar /yuanyl/sentinel/sentinel-dashboard-1.6.1.jar > ./logs/sentinel.log 2>&1 &
#nohup java $JAVA_OPT -jar ./sentinel-dashboard.jar &

#其中 -Dserver.port=8080 用于指定 Sentinel 控制台端口为 8080。
#从 Sentinel 1.6.0 起，Sentinel 控制台引入基本的登录功能，默认用户名和密码都是 sentinel。
```

​	**rabbitmq**

```shell
docker run -d --name rabbitmq -p 5671:5671 -p 5672:5672 -p 4369:4369 -p 25672:25672 -p 15671:15671 -p 15672:15672 -p 15674:15674 -p 15670:15670 -p 15673:15673 --restart=always xiaochunping/rabbitmq:management 
```

> 访问服务器的15672端口，默认账号、密码是guest，访问成功则创建成功

​	**nacos，promehteus，grafana**

```shell
version: "2"
services:
  nacos:
    image: nacos/nacos-server:latest
    container_name: nacos-standalone-mysql
    environment:
      - PREFER_HOST_MODE=hostname
      - MODE=standalone
      - SPRING_DATASOURCE_PLATFORM=mysql
      - MYSQL_MASTER_SERVICE_HOST=127.0.0.1
      - MYSQL_MASTER_SERVICE_DB_NAME=nacos_config
      - MYSQL_MASTER_SERVICE_PORT=3307
      - MYSQL_SLAVE_SERVICE_HOST=127.0.0.1
      - MYSQL_SLAVE_SERVICE_PORT=3308
      - MYSQL_MASTER_SERVICE_USER=root
      - MYSQL_MASTER_SERVICE_PASSWORD=Yuanyulou789!
    volumes:
      - ./standalone-logs/:/home/nacos/logs
      - ./init.d/custom.properties:/home/nacos/init.d/custom.properties
    ports:
      - "8848:8848"
      - "9555:9555"
    restart: on-failure
  prometheus:
    container_name: prometheus
    image: prom/prometheus:latest
    volumes:
      - ./prometheus/prometheus-standalone.yaml:/etc/prometheus/prometheus.yml
    ports:
      - "9090:9090"
    depends_on:
      - nacos
    restart: on-failure
  grafana:
    container_name: grafana
    image: grafana/grafana:latest
    ports:
      - 3000:3000
    restart: on-failure
```



```
# 初次启动用up
docker-compose -f /xxx/standalone-mysql-study.yaml up/start
```



## 项目搭建

```
mvn clean package -Dmaven.test.skip=true -U
```



