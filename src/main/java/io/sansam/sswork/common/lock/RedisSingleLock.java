package io.sansam.sswork.common.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.commands.JedisCommands;
import redis.clients.jedis.params.SetParams;

import java.util.Objects;

/**
 * redis单机 分布式锁
 */
@Component
@Slf4j
public class RedisSingleLock implements DistributedLock {


    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public String lock(String key, String val, long expire, long timeOut) {

        Object result = redisTemplate.execute((RedisCallback) redisConnection -> {
            JedisCommands commands = (JedisCommands) redisConnection.getNativeConnection();
            SetParams params = new SetParams();
            // Set the specified expire time, in milliseconds.
            params.nx();
            // Set the specified expire time, in milliseconds.
            params.px(3 * timeOut);
            return commands.set(key, val, params);
        });
        return Objects.isNull(result) ? "加锁失败" : result.toString();
    }

    @Override
    public boolean release(String key) {
        return false;
    }
}
