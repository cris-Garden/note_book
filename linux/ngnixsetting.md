- [查找nginx的配置文件](#查找nginx的配置文件)
  - [1. 查看nginx路径](#1-查看nginx路径)
  - [2. 查看nginx配置文件路径](#2-查看nginx配置文件路径)
- [nginx 配置多个server,多个端口](#nginx-配置多个server多个端口)

# 查找nginx的配置文件

nginx的配置放在nginx.conf文件中，一般我们可以使用以下命令查看服务器中存在的nginx.conf文件。

```S
locate nginx.conf
/usr/local/etc/nginx/nginx.conf
/usr/local/etc/nginx/nginx.conf.default
...
```

如果服务器中存在多个nginx.conf文件，我们并不知道实际上调用的是哪个配置文件，因此我们必须找到实际调用的配置文件才能进行修改。


查看nginx实际调用的配置文件

## 1. 查看nginx路径
   
```shell
ps aux|grep nginx
root              352   0.0  0.0  2468624    924   ??  S    10:43上午   0:00.08 nginx: worker process  
root              232   0.0  0.0  2459408    532   ??  S    10:43上午   0:00.02 nginx: master process /usr/local/opt/nginx/bin/nginx -g daemon off;  
root             2345   0.0  0.0  2432772    640 s000  S+    1:01下午   0:00.00 grep nginx

```
nginx的路径为：`/usr/local/opt/nginx/bin/nginx`


## 2. 查看nginx配置文件路径
使用nginx的 -t 参数进行配置检查，即可知道实际调用的配置文件路径及是否调用有效。
```shell

/usr/local/opt/nginx/bin/nginx -t
nginx: the configuration file /usr/local/etc/nginx/nginx.conf syntax is ok
nginx: configuration file /usr/local/etc/nginx/nginx.conf test is successful
```

测试可知，nginx的配置文件路径为：`/usr/local/etc/nginx/nginx.conf` 且调用有效。

# nginx 配置多个server,多个端口
现在同一linux服务器配置其端口的服务，拟用已经运行 的nginx来配置

步骤如下

创建一个配置文件  比如  /usr/local/nginx/sites-available/gaotianyue.conf

```properties
upstream gaotianyue.com{
        server 127.0.0.1:8070 weight=1;
    }
    server {
        listen       8071;
        server_name  localhost;
 
        location / {
            proxy_http_version 1.1;
            proxy_pass http://gaotianyue.com;
            proxy_redirect default;
            proxy_connect_timeout 1s;
            proxy_read_timeout 5s;
            proxy_send_timeout 2s;
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header Connection "upgrade";
            add_header 'Access-Control-Allow-Headers' 'Content-Type';
            add_header 'Access-Control-Allow-Origin' '*';
            add_header 'Access-Control-Allow-Methods' 'GET';
        }
 
        location ~ .*\.(html|htm|gif|jpg|jpeg|bmp|png|ico|txt|js|css|mp3|mp4)$
        {
             root /data/gaotianyue/static_src;
             expires      14d;
        }
 
         error_page  404   /404.html;
         error_page  404   /404.html;
         location = /404.html{
          root   html;
        }
 
        error_page   500 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
        error_page   502  /502.html;
        location = /502.html{
            root   html;
        }
   }

```
修改nginx.conf
最后一个括号前添加
```

  include /usr/local/nginx/sites-available/*.conf;
}
```

然后检查下
```
[root@VM_126_4_centos nginx]# sbin/nginx -t -c conf/nginx.conf
nginx: the configuration file /usr/local/nginx/conf/nginx.conf syntax is ok
nginx: configuration file /usr/local/nginx/conf/nginx.conf test is successful

```
最后重启

```
sbin/nginx -s reload
```
