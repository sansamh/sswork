# sswork
- RestController统一返回值处理

使用@ResponseResult注解在类或者方法上，此方法返回值会被包装成Result对象。

- Redis分布式锁

采用Jedis实现了单机版本的Redis分布式锁，Redisson实现了Redis-Cluster集群模式的分布式锁。未来可以加上注解实现AOP自动获取锁的方法。
参考 [KLock](https://gitee.com/kekingcn/spring-boot-klock-starter)的实现。

- 下一步准备做使用MQ处理订单超时关闭的任务。