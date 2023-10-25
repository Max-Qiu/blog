FROM eclipse-temurin:17.0.6_10-jdk-centos7
ENV TZ=Asia/Shanghai
WORKDIR /blog
ADD ./target/resources ./resources
ADD ./target/lib ./lib
ADD ./target/blog.jar ./
CMD java -Dloader.path=./lib -XX:+PrintCommandLineFlags -Xmx128m -XX:+UseZGC -jar blog.jar --spring.profiles.active=pro
