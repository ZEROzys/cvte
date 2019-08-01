## Centos6.8 搭建RocketMQ踩坑记录

为了学习RocketMQ，决定在自己的云服务器上搭建RocketMQ

服务器搭建RocketMQ，这里跟着官方文档走就好了

https://rocketmq.apache.org/docs/quick-start/

这里需要注意的是，需要把  /distribution/target/apache-rocketmq/bin目录下的 runserver.sh 和 runbroker.sh 的内存修改一下，默认内存太大了，小服务器遭不住啊

这里放出我的仅供参考

```
JAVA_OPT="${JAVA_OPT} -server -Xms256m -Xmx256m -Xmn128m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m"
```

这里主要叙述一下，IDEA跑官方提供的 Simple Example遇到的各种坑

1. 首先记得把安全组和防火墙的 **9876、10909、10911** 端口全部开放

2. 启动 namesrv 时的命令为

   ```
   nohup bin/mqnamesrv -n 你的公网IP:9876 &
   ```

3. 启动 broker 时，首先需要在 conf/broker.conf 中加入 brokerIP1 = 你的公网IP（注意大小写，我哭了），然后命令

   ```
   nohup sh bin/mqbroker -n 你的公网IP:9876 -c conf/broker.conf autoCreateTopicEnable=true &
   ```

