FROM eclipse-temurin:25.0.1_8-jdk
ENV TZ=Asia/Shanghai
WORKDIR /blog
ADD ./target/resources ./resources
ADD ./target/lib ./lib
ADD ./target/blog.jar ./
CMD ["java","-Dloader.path=./lib","-XX:+PrintCommandLineFlags","-jar","blog.jar","--spring.profiles.active=pro"]
