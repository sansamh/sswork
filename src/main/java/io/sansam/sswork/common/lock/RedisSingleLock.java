package io.sansam.sswork.common.lock;

import io.sansam.sswork.common.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.commands.JedisCommands;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * redis单机 分布式锁
 */
@Component
@Slf4j
public class RedisSingleLock implements DistributedLock {


    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public boolean lock(String key, String val, long expire, TimeUnit timeUnit) {

        Object result = redisTemplate.execute((RedisCallback) redisConnection -> {
            JedisCommands commands = (JedisCommands) redisConnection.getNativeConnection();
            SetParams params = new SetParams();
            params.nx();
            // Set the specified expire time, in milliseconds.
            if (TimeUnit.SECONDS.equals(timeUnit)) {
                params.ex(Integer.valueOf(Objects.toString(expire)));
            } else if (TimeUnit.MILLISECONDS.equals(timeUnit)) {
                params.px(expire);
            } else {
                log.error("RedisSingleLock - lock 无法处理的TimeUnit {}", timeUnit);
                return null;
            }
            return commands.set(key, val, params);
        });
        // 成功返回OK
        return !Objects.isNull(result) && CommonConstant.OK.equals(result.toString());
    }

    @Override
    public boolean release(String key, String val) {
        //lua script
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object execute = redisTemplate.execute((RedisCallback) redisConnection -> {
            JedisCommands commands = (JedisCommands) redisConnection.getNativeConnection();
            if (commands instanceof Jedis) {
                return ((Jedis) commands).eval(script, Collections.singletonList(key), Collections.singletonList(val));
            } else if (commands instanceof JedisCluster) {
                return ((JedisCluster) commands).eval(script, Collections.singletonList(key), Collections.singletonList(val));
            } else {
                return 0L;
            }
        });
        if (Objects.isNull(execute)) {
            return false;
        }
        // 1成功 0失败
        return CommonConstant.ONE.equals(execute.toString());
    }

    @Override
    public boolean tryLock(String key, String val, long expire, long timeOut, TimeUnit timeUnit) {


        return false;
    }
}
