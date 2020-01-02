### 使用Dubbo构建一个RPC服务

#### 准备工作
安装Zookeepr并启动

#### 创建一个interface
1. 新建一个Module名称为dubbo-interface
2. 创建HelloService接口（服务端负责实现接口，客户端调用该接口）
```java
public interface HelloService {
    public String sayHello(String name);
}
```

#### 创建Provider
1. 新建一个Module名称为dubbo-provider
2. pom.xml中引入dubbo和zookeeper依赖
```xml
<!--引入dubbo的依赖-->
<dependency>
    <groupId>com.alibaba.spring.boot</groupId>
    <artifactId>dubbo-spring-boot-starter</artifactId>
    <version>2.0.0</version>
</dependency>

<!-- 引入zookeeper的依赖 -->
<dependency>
    <groupId>com.101tec</groupId>
    <artifactId>zkclient</artifactId>
    <version>0.10</version>
</dependency>
```
3. 引入HelloService依赖
```xml
<dependency>
    <groupId>com.shgx</groupId>
    <artifactId>dubbo-interface</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
4. 创建一个服务，负责实现HelloService逻辑
```java
@Component
@Service(version = "1.0.0", timeout = 3000)
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
```
5. 在application.yml完成相关配置
```yml
spring:
  dubbo:
    application:
      name: provider
    server: true
    registry:
      address: zookeeper://127.0.0.1:2181
    protocol:
      name: dubbo
      port: 20880
```
6. main中开启相关的配置并注册服务
```java
@SpringBootApplication
// 开启dubbo的自动配置
@EnableDubboConfiguration
public class DubboProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(DubboProviderApplication.class, args);
    }

}
```
#### 创建Consumer
1. 新建一个Module名称为dubbo-consumer
2. 依赖的导入同Provider中的1和2
3. 创建Controller调用服务端的HelloService
```java
@RestController
public class HelloController {

    @Reference(version = "1.0.0", timeout = 3000)
    private HelloService helloService;

    @RequestMapping("/hello")
    public String hello() {
        String hello = helloService.sayHello("world");
        System.out.println(helloService.sayHello("Hello Dubbo!!!"));
        return hello;
    }
}
```
4. 在application.yml完成相关配置
```yml
spring:
  dubbo:
    application:            #应用配置，用于配置当前应用信息，不管该应用是提供者还是消费者。
      name: consumer
    registry:                 #注册中心配置，用于配置连接注册中心相关信息。
      address: zookeeper://127.0.0.1:2181
    protocol:     #协议配置，用于配置提供服务的协议信息，协议由提供方指定，消费方被动接受（订阅）。
      name: dubbo
      port: 20880
    consumer:
      check: false
    reference:
      loadbalance: roundrobin #轮询机制
      #loadbalance: random #随机机制
      #loadbalance: leastactive #最少活跃调用数机制
```
5. main中开启相关的配置
```java
@SpringBootApplication
@EnableDubboConfiguration
public class DubboConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DubboConsumerApplication.class, args);
    }

}
```

6. 输入```http://localhost:8083/hello```调用配置

返回"Hello World"并打印"Hello Dubbo!!!"

#### 踩坑
- SpringBoot版本不能太高，建议2.0.3.RELEASE，Dubbo版本2.0.0，Zookeeper版本10.0
- Provider中的@Service是import com.alibaba.dubbo.config.annotation.Service;包
