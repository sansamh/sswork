# sswork
- RestController统一返回值处理

使用@ResponseResult注解在类或者方法上，此方法返回值会被包装成Result对象。

- Redis分布式锁

采用Jedis实现了单机版本的Redis分布式锁，Redisson实现了Redis-Cluster集群模式的分布式锁。未来可以加上注解实现AOP自动获取锁的方法。
参考 [KLock](https://gitee.com/kekingcn/spring-boot-klock-starter)的实现。

- MySQL 单个实例 多数据库事务测试
 
经过测试，同个MySQL实例，多个数据库之间是同一个事务，无需指定多个数据源和事务管理器。
```
配置两个数据源DBConfigOne和DBConfigTwo，分别对应jdbc:mysql://118.25.210.217:13306/test和jdbc:mysql://118.25.210.217:13306/app
测试发现需要在SQL表名前指定库名，比如app.student、test.score，这样才能查询到数据
同时去掉DBConfigTwo配置的时候，也可以成功操作。
```
同时解决了Spring事务相关疑惑，详情见note - Spring里《Spring事务详解》

- 下一步准备做使用MQ处理订单超时关闭的任务。

