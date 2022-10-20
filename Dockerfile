FROM openjdk:11.0.13-jdk-bullseye
ENV TZ=Asia/Shanghai
WORKDIR /blog
ADD ./target/resources ./resources
ADD ./target/lib ./lib
ADD ./target/blog.jar ./
CMD java -Dloader.path=./lib -XX:+PrintCommandLineFlags -Xms128m -Xmx128m -Xmn64m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m -XX:SurvivorRatio=8 -XX:+UseG1GC -jar blog.jar --spring.profiles.active=pro
