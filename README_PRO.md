> 生产环境部署时，建议先使用本地虚拟机做测试

## 基础环境搭建

- [CentOS7配置jdk](https://maxqiu.com/article/detail/4)（需要安装JDK11）
- [CentOS7配置Maven3.6.3](https://maxqiu.com/article/detail/6)
- [CentOS7使用源码包编译安装Git](https://maxqiu.com/article/detail/104)
- [CentOS7安装docker-ce（阿里源）](https://maxqiu.com/article/detail/24)

## 基础服务搭建

> 以下服务在生产环境若有专用的云服务，则跳过安装

- [Docker常用镜像：MySQL](https://maxqiu.com/article/detail/32)
- [Docker常用镜像：Redis](https://maxqiu.com/article/detail/51)
- [Docker常用镜像：Elasticsearch](https://maxqiu.com/article/detail/54)

特别说明：

- 安装服务前使用 `docker network create -d bridge blog-bridge` 创建网络
- 安装服务时，所有服务均使用 `--network blog-bridge` 连接到同一网络内
- `MySQL` 需要对外开放端口，用于导入数据等操作
- `Redis` 不需要对外开放，除非是和其他服务不在同一机器

## 数据库

MySQL：连接服务端数据库

- 导入源码 `db` 目录下的 `blog.sql`
- （推荐）为当前项目分配一个单独的用户名密码

## 创建文件上传目录

```bash
mkdir -p /work/blog-files/upload
```

## 启动服务

上传代码

```bash
# 创建代码文件夹
mkdir -p /work/project
# 进入该文件夹
cd /work/project/
# 拉取源码（或者手动上传）
git clone xxxxxx
```

编译部署

```bash
# 进入项目目录
cd blog/
# 启动容器（包含拉取代码、Maven打包、制作镜像、启动容器。若不需要拉取代码，则修改脚本注释掉）
./depoly.sh
```

启动之后 `docker stats` 查看CPU占用，一段时间（一两分钟）后CPU占用回归零点几、一点几；若发现服务有重启的情况，肯定是部分配置错误，使用 `docker logs -f blog`
查看日志排查问题

## 启动Nginx

创建Nginx目录

```bash
mkdir -p /work/nginx
cd /work/nginx/
```

### 配置文件

创建Nginx配置文件

```bash
vim nginx.conf
```

内容如下：

```
user  nginx;
worker_processes  1;
error_log  /var/log/nginx/error.log warn;
pid        /var/run/nginx.pid;
events {
    worker_connections  1024;
}
http {
    include       /etc/nginx/mime.types;
    default_type  application/octet-stream;
    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';
    access_log  /var/log/nginx/access.log  main;
    sendfile        on;
    #tcp_nopush     on;
    keepalive_timeout  65;
    gzip  on;
    gzip_min_length 1k;
    gzip_buffers 4 16k;
    gzip_comp_level 3;
    gzip_types text/plain text/css application/xml application/javascript application/x-javascript text/javascript;
    client_max_body_size 20m;
    server {
        listen 80;
        # 拦截乱七八糟的请求
        set $flag f;
        if ($request_uri ~* "^/(|sitemap.txt|favicon.ico)$") {
              set $flag "true";
        }
        if ($request_uri ~* "^/(article|about|assets|upload|_admin).*$") {
              set $flag "true";
        }
        if ($flag != "true" ){
            return 404;
        }
        location / {
            proxy_pass http://blog:30001/;
            add_header Strict-Transport-Security "max-age=31536000";
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header Host $http_host;
            proxy_set_header HTTP_X_FORWARDED_FOR $remote_addr;
            proxy_set_header X-Forwarded-Proto $scheme;
            proxy_redirect default;
            sub_filter_once off;
        }
    }
}
```

### Dockerfile

新建Dockerfile

```bash
vim Dockerfile
```

内容如下

```
FROM nginx:1.20.1
ENV TZ=Asia/Shanghai
COPY nginx.conf /etc/nginx/nginx.conf
```

### 启动脚本

新建启动脚本

```
vim build.sh 
```

内容如下

```bash
#!/bin/bash
docker build -t blog-nginx .
docker rm -f blog-nginx
docker run -d --restart always --name blog-nginx --network blog-bridge -p 80:80 -p 443:443 blog-nginx:latest
```

授权

```bash
chmod 744 build.sh
```

### 执行

```bash
./build.sh 
```

## 访问测试

浏览器打开网址，测试是否正常访问！
