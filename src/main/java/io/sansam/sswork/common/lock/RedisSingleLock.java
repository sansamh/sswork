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
        try {
            Object result = redisTemplate.execute((RedisCallback) redisConnection -> {
                JedisCommands commands = (JedisCommands) redisConnection.getNativeConnection();
                SetParams params = new SetParams();
                params.nx();
                // Set the specified expire time, in milliseconds.
                if (TimeUnit.SECONDS.equals(timeUnit)) {
                    params.ex(Integer.parseInt(Objects.toString(expire)));
                } else if (TimeUnit.MILLISECONDS.equals(timeUnit)) {
                    params.px(expire);
                } else {
                    log.error("RedisSingleLock - lock key = [{}] 无法处理的TimeUnit [{}]", key, timeUnit);
                    return null;
                }
                return commands.set(key, val, params);
            });
            // 成功返回OK
            log.info("RedisSingleLock - lock key = [{}] result = [{}]", key, result);
            return !Objects.isNull(result) && CommonConstant.OK.equals(result.toString());
        } catch (Exception e) {
            log.error("RedisSingleLock - lock key = [{}] caught Exception ", key, e);
            return false;
        }

    }

    @Override
    public boolean release(String key, String val) {
        try {
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
            log.info("RedisSingleLock - release key = [{}] result = [{}]", key, execute);
            if (Objects.isNull(execute)) {
                return false;
            }
            // 1成功 0失败
            return CommonConstant.ONE.equals(execute.toString());
        } catch (Exception e) {
            log.error("RedisSingleLock - release key = [{}] caught Exception ", key, e);
            return false;
        }
    }

    @Override
    public boolean tryLock(String key, String val, long expire, long timeOut, TimeUnit timeUnit) {
        try {
            long begin = System.currentTimeMillis();
            boolean lock = this.lock(key, val, expire, timeUnit);
            if (lock) {
                return lock;
            }
            long now = System.currentTimeMillis();
            long ttl = now - begin;
            while (ttl > 0) {
                lock = this.lock(key, val, expire, timeUnit);
                if (lock) {
                    return lock;
                }
                now = System.currentTimeMillis();
                ttl = now - ttl;
            }
            return false;
        } catch (Exception e) {
            log.error("RedisSingleLock - tryLock key = [{}] caught Exception ", key, e);
            return false;
        }
    }
}
