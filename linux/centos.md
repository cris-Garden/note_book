- [一，下载jdk14](#一下载jdk14)
  - [点击jdk download,进入下载页面:](#点击jdk-download进入下载页面)
- [二，安装jdk14](#二安装jdk14)
- [三，配置java的环境变量:](#三配置java的环境变量)
- [四，检查java安装的效果](#四检查java安装的效果)
- [五，查看centos的版本](#五查看centos的版本)
- [查看操作系统多少位](#查看操作系统多少位)
- [查看CPU信息，机器型号，内存等信息](#查看cpu信息机器型号内存等信息)



linux(centos8):安装java jdk 14 (java 14.0.2)

# 一，下载jdk14
官方网站:
```
https://www.oracle.com/java/
```
 

下载页面:
```
https://www.oracle.com/cn/java/technologies/javase-downloads.html
```

## 点击jdk download,进入下载页面:

选择这个版本下载：
```S
jdk-14.0.2_linux-x64_bin.tar.gz
```
 
下载后保存到
/usr/local/source/java
说明：我们不要把安装文件（源码，安装包）和安装后运行的程序放到一起，

应该分开存放

查看当前目录

```shell
[root@kubemaster java]# pwd
/usr/local/source/java
[root@kubemaster java]# ls
jdk-14.0.2_linux-x64_bin.tar.gz
```
 

说明：刘宏缔的架构森林是一个专注架构的博客，
地址：https://www.cnblogs.com/architectforest

对应的源码可以访问这里获取： https://github.com/liuhongdi/

 
# 二，安装jdk14
1，解压安装文件的压缩包：
```
[root@kubemaster java]# tar -zxvf jdk-14.0.2_linux-x64_bin.tar.gz
```
 

2，把解压后的jdk目录，移动到安装目录/usr/local/soft目录下:
```
[root@kubemaster java]# mv jdk-14.0.2 /usr/local/soft/
```
 

# 三，配置java的环境变量:
编辑profile文件,把命令保存到文件中

```shell
[root@kubemaster java]# vi /etc/profile
```
 
内容:
```shell
export JAVA_HOME=/usr/local/soft/jdk-14.0.2
export JRE_HOME=${JAVA_HOME}/jre
export CLASSPATH=.:$JAVA_HOME/lib:$JRE_HOME/lib
export PATH=$PATH:$JAVA_HOME/bin:$JRE_HOME/bin
```
 

使变量生效
```
[root@kubemaster java]# source /etc/profile
```
 

说明：jre默认目录不存在，可用下面的命令生成jre的目录:

```Shell
[root@kubemaster lib]# cd /usr/local/soft/jdk-14.0.2/
[root@kubemaster jdk-14.0.2]# ./bin/jlink --module-path jmods --add-modules java.desktop --output jre
```
 

# 四，检查java安装的效果
```Shell
[root@kubemaster liuhongdi]# java --version
java 14.0.2 2020-07-14
Java(TM) SE Runtime Environment (build 14.0.2+12-46)
Java HotSpot(TM) 64-Bit Server VM (build 14.0.2+12-46, mixed mode, sharing)
```
 可以看到java的版本:14.0.2

 

# 五，查看centos的版本
```
[liuhongdi@kubemaster java]$ cat /etc/redhat-release
CentOS Linux release 8.2.2004 (Core) 
```

# 查看操作系统多少位
```S
getconf LONG_BIT
```
# 查看CPU信息，机器型号，内存等信息
 系统

```
# uname -a               # 查看内核/操作系统/CPU信息
# lsb_release -a         # 查看操作系统版本 (适用于所有的linux，包括Redhat、SuSE、Debian等发行版，但是在debian下要安装lsb)   
# cat /proc/cpuinfo      # 查看CPU信息
# hostname               # 查看计算机名
# lspci -tv              # 列出所有PCI设备
# lsusb -tv              # 列出所有USB设备
# lsmod                  # 列出加载的内核模块
# env                    # 查看环境变量
```
资源
```

# free -m                # 查看内存使用量和交换区使用量
# df -h                  # 查看各分区使用情况
# du -sh <目录名>        # 查看指定目录的大小
# grep MemTotal /proc/meminfo   # 查看内存总量
# grep MemFree /proc/meminfo    # 查看空闲内存量
# uptime                 # 查看系统运行时间、用户数、负载
# cat /proc/loadavg      # 查看系统负载
```
磁盘和分区

```
# mount | column -t      # 查看挂接的分区状态
# fdisk -l               # 查看所有分区
# swapon -s              # 查看所有交换分区
# hdparm -i /dev/hda     # 查看磁盘参数(仅适用于IDE设备)
# dmesg | grep IDE       # 查看启动时IDE设备检测状况
```
网络

```
# ifconfig               # 查看所有网络接口的属性
# iptables -L            # 查看防火墙设置
# route -n               # 查看路由表
# netstat -lntp          # 查看所有监听端口
# netstat -antp          # 查看所有已经建立的连接
# netstat -s             # 查看网络统计信息
```
进程

```
# ps -ef                 # 查看所有进程
# top                    # 实时显示进程状态
```
用户

```
# w                      # 查看活动用户
# id <用户名>            # 查看指定用户信息
# last                   # 查看用户登录日志
# cut -d: -f1 /etc/passwd   # 查看系统所有用户
# cut -d: -f1 /etc/group    # 查看系统所有组
# crontab -l             # 查看当前用户的计划任务
```
服务

```
# chkconfig --list       # 列出所有系统服务
# chkconfig --list | grep on    # 列出所有启动的系统服务
```
程序

```
# rpm -qa                # 查看所有安装的软件包
```

查看CPU信息（型号）
```
# cat /proc/cpuinfo | grep name | cut -f2 -d: | uniq -c
      8  Intel(R) Xeon(R) CPU            E5410   @ 2.33GHz
```
(看到有8个逻辑CPU, 也知道了CPU型号)
```

# cat /proc/cpuinfo | grep physical | uniq -c
      4 physical id      : 0
      4 physical id      : 1
```
(说明实际上是两颗4核的CPU)

```
# getconf LONG_BIT
   32
```
(说明当前CPU运行在32bit模式下, 但不代表CPU不支持64bit)

```
# cat /proc/cpuinfo | grep flags | grep ' lm ' | wc -l
   8
```
(结果大于0, 说明支持64bit计算. lm指long mode, 支持lm则是64bit)


再完整看cpu详细信息, 不过大部分我们都不关心而已.
```
# dmidecode | grep 'Processor Information'
```

查看内 存信息
```
# cat /proc/meminfo

# uname -a
Linux euis1 2.6.9-55.ELsmp #1 SMP Fri Apr 20 17:03:35 EDT 2007 i686 i686 i386 GNU/Linux
(查看当前操作系统内核信息)

# cat /etc/issue | grep Linux
Red Hat Enterprise Linux AS release 4 (Nahant Update 5)
(查看当前操作系统发行版信息)
```

查看机器型号
```
# dmidecode | grep "Product Name" 
```

查看网卡信息
```
# dmesg | grep -i eth
```