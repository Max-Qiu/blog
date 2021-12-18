> 麦克斯博客

线上环境：Max_Qiu的博客 [https://maxqiu.com](https://maxqiu.com)

源码仓库地址：

- [https://github.com/Max-Qiu/min-blog](https://github.com/Max-Qiu/min-blog)
- [https://gitee.com/Max-Qiu/min-blog](https://gitee.com/Max-Qiu/min-blog)

PS：微服务版的折腾完了，还是转回单体应用

# 技术介绍

## 运行环境

软件 | 版本
---|---
CentOS | 7.6
Docker | 20.10.7
JDK | 11
Maven | 3.6.3
MySQL | 8.0.25
Redis | 6.2.4
ElasticSearch | 7.9.3
Nginx | 1.20.1

## 框架版本

框架 | 版本
---|---
SpringBoot | 2.6.1
Mybatis Plus | 3.4.3.4
Fastjson | 1.2.78
Hutool | 5.7.16

# 部署说明

> 本人线上环境如下

系统 | CPU | 内存
---|---|---
CentOS7.6 | 2核 | 4 GB

- 如果是一台 `1 GB` 内存的服务器，肯定不够跑
- 如果是一台 `2 GB` 内存的服务器，基本能跑
- 如果是一台 `4 GB` 内存的服务器，无压力

## 生产环境部署说明（CentOS7）

[生产环境部署说明](README_PRO.md)

## 生产环境进阶说明（CentOS7）

- 评论邮箱通知
- IP地址信息查询
- 七牛云cdn加速
- https

待补充（东西很多，有需要再整理）
