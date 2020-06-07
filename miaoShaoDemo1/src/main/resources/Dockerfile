# 该镜像需要依赖的基础镜像
FROM java:8

# VOLUME 指定了临时文件目录为/tmp
VOLUME /tmp

# 将当前目录下的jar包复制到docker容器的/目录下
ADD @artifactId@-@version@.jar /temp/@artifactId@-@version@.jar

# 运行过程中创建一个jar文件
RUN bash -c 'touch @artifactId@-@version@.jar'

# 声明服务运行在8080端口
EXPOSE 8080

# 指定docker容器启动时运行jar包
ENTRYPOINT ["java", "-jar","/temp/@artifactId@-@version@.jar"]

# 指定维护者的名字
MAINTAINER lizhou828