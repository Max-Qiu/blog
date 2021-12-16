FROM openjdk:11.0.13-jdk-bullseye
ENV TZ=Asia/Shanghai
WORKDIR /min-blog
ADD ./target/resources ./resources
ADD ./target/min-blog.jar ./
CMD java -server -Xms128m -Xmx128m -Xmn32m -Xss512k -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m -jar min-blog.jar --spring.profiles.active=pro
