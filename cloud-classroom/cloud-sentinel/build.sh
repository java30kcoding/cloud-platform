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

nohup java $JAVA_OPT -jar /yuanyl/sentinel/sentinel-dashboard-1.6.1.jar > ./logs/sentinel.log 2>&1 &
#nohup java $JAVA_OPT -jar ./sentinel-dashboard.jar &

#其中 -Dserver.port=8080 用于指定 Sentinel 控制台端口为 8080。
#从 Sentinel 1.6.0 起，Sentinel 控制台引入基本的登录功能，默认用户名和密码都是 sentinel。
