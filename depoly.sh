#!/bin/bash
cd /work/project/blog/ || exit
git pull
mvn clean package
VERSION=$(date "+%Y%m%d")
docker build -t blog:"$VERSION" .
docker rm -f blog
docker run -d --restart always --name blog -v /work/log/blog:/log -v /work/blog-files/:/blog-files --network blog-bridge -p 30001:30001 blog:"$VERSION"
docker image prune -f
