# 微服务负载均衡实验

本项目实现了一个微服务集群，包含3个服务提供者和3个使用不同负载均衡策略的消费者。

## 项目结构

```
service-cluster/
├── provider-service-1/     # 服务提供者1，返回"1"
├── provider-service-2/     # 服务提供者2，返回"2"
├── provider-service-3/     # 服务提供者3，返回"3"
├── consumer-round-robin/   # 使用轮询负载均衡策略的消费者
├── consumer-random/        # 使用随机负载均衡策略的消费者
├── consumer-weighted/      # 使用权重负载均衡策略的消费者
└── pom.xml                 # 父项目POM
```

## 实验要求

1. 搭建服务集群，包含3个提供者和1个注册中心
2. 实现3种不同的消费者：
   - 轮询消费者：按顺序1、2、3依次调用服务
   - 随机消费者：随机顺序调用服务
   - 自定义权重消费者：按照权重分配（服务1：10%，服务2：30%，服务3：60%）

## 环境要求

- JDK 8+
- Maven 3.5+
- Nacos 服务器：192.168.11.10:8848

## 运行说明

1. 启动Nacos服务器
2. 分别启动三个服务提供者
   ```
   cd provider-service-1
   mvn spring-boot:run
   
   cd provider-service-2
   mvn spring-boot:run
   
   cd provider-service-3
   mvn spring-boot:run
   ```
   
3. 分别启动三个服务消费者
   ```
   cd consumer-round-robin
   mvn spring-boot:run
   
   cd consumer-random
   mvn spring-boot:run
   
   cd consumer-weighted
   mvn spring-boot:run
   ```

4. 测试各个消费者的负载均衡效果
   - 轮询：http://localhost:9001/consume
   - 随机：http://localhost:9002/consume
   - 权重：http://localhost:9003/consume 