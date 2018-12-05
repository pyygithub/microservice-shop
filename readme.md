## 项目介绍

微信商城+微信公众号开发，该项目根据上海某大型知名电商企业互联网互联网项目，改版而来，使用最新微服务技术，页面使用高仿小米界面。
功能包含电商模块,会员、SSO、订单、商品、支付、消息、微信、H5和PC、移动端、优惠券、后台系统、任务调度等模块。

## 项目拆分

在大型电商项目中,会将一个大的项目，拆分成N多个子模块，分配给不同的团队开发。
团队之间通讯采用RPC远程调用技术、使用Http+Restful+Json传输。

## 接口服务

会员服务、订单服务、商品服务、支付服务、消息服务、秒杀服务、优惠券服务等。
项目工程
H5项目、微信项目、后台管理等。

## 使用技术

电商项目
- SpringBoot+SpringCloud +Maven+Redis+ActiveMQ+XXLJOB(分布式任务调度)+ Freemarker等。
- 使用 SpringCloud Eureka作为注册中心、Feign客户端调用工具、断路器Hystrix
- 视图展示使用Freemarker、数据库层使用Mybatis框架、缓存使用Redis、数据库使用MySQL
- 项目管理工具使用Maven、版本控制工具使用SVN、项目自动部署工具使用Jenkins
- 消息中间件使用ActiveMQ、分布式任务调度系统使用XXLJOB、反向代理工具使用Nginx
- 日志管理插件工具使用lombok、分布式日志收集使用Logstash、解析JSON框架使用FastJson
- 数据安全加密使用MD5加盐和Base64、RSA、分布式文件存储系统FastDFS等。
- 支付网关接口使用支付宝、第三方登录使用QQ授权等。

## 项目构建
```
microservice-shop-parent
  ├─microservice-shop-common                   // 抽取的公共模块
  ├─microservice-shop-eurekaserver             // eureka注册中心
  ├─microservice-shop-api                      // api服务              
     ├─microservice-shop-member-api            // 会员服务
     ├─microservice-shop-order-api             // 订单服务
     ├─microservice-shop-goods-api             // 商品服务
     ├─microservice-shop-pay-api               // 支付服务
  ├─microservice-shop-member                   // 会员系统
  ├─microservice-shop-nessage                  // 消息系统
  ├─microservice-shop-mobile-web               // h5端工程

```
