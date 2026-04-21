# # 第一阶段：编译环境 (使用 Maven 镜像)
# FROM maven:3.8.4-openjdk-17 AS build
# WORKDIR /app

# # 将 pom.xml 和源码拷贝进容器
# COPY pom.xml .
# RUN mvn dependency:go-offline

# COPY src ./src

# # 执行打包命令，跳过测试
# RUN mvn clean package -DskipTests

# # 第二阶段：运行环境（使用完整 JDK 镜像）
# FROM eclipse-temurin:17

# RUN apt-get update && \
#     apt-get install -y locales && \
#     locale-gen zh_CN.UTF-8

# WORKDIR /app

# # 设置时区
# ENV TZ=Asia/Shanghai

# # 拷贝 jar 包
# COPY --from=build /app/target/*.jar app.jar

# # 暴露端口
# EXPOSE 8080

# # 启动 Spring Boot
# ENTRYPOINT ["java", "-jar", "app.jar"]


# 直接使用基础运行环境
FROM eclipse-temurin:17

# RUN apt-get update && \
#     apt-get install -y locales && \
#     locale-gen zh_CN.UTF-8

WORKDIR /app

# 设置时区
ENV TZ=Asia/Shanghai

# 直接把宿主机 target 目录下已经构建好的 jar 包复制进容器
# 前提：你必须先在本地执行过打包命令
COPY target/*.jar app.jar

# 暴露端口
EXPOSE 8080

# 启动 Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
