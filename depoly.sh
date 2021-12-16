#!/bin/bash
cd /work/project/min-blog/
git pull
mvn clean package
VERSION=$(date "+%Y%m%d")
docker build -t min-blog:$VERSION .
docker rm -f min-blog
docker run -d --restart always --name min-blog -v /work/log/min-blog:/log -v /work/min-blog-files/:/min-blog-files --network min-blog-bridge min-blog:$VERSION
docker image prune -f
docker restart nginx
