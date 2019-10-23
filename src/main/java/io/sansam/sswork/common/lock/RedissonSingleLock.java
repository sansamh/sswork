package io.sansam.sswork.common.lock;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * RedissonSingleLock
 * </p>
 *
 * @author houcb
 * @since 2019-10-23 15:18
 */
@Slf4j
@Component
public class RedissonSingleLock implements DistributedLock {

    @Autowired
    @Qualifier(value = "singleRedissonClient")
    RedissonClient singleRedissonClient;

    @Override
    public boolean lock(String key, String val, long expire, TimeUnit timeUnit) {
        if (Objects.isNull(singleRedissonClient)) {
            log.error("RedissonSingleLock - lock singleRedissonClient is null");
            return false;
        }
        try {
            RLock lock = singleRedissonClient.getLock(key);
            lock.lock(expire, timeUnit);
            log.info("Thread [{}] RedissonSingleLock - lock [{}] success", Thread.currentThread().getName(), key);
            return true;

        } catch (Exception e) {
            log.error("RedissonSingleLock - lock [{}] Exception:", key, e);
            return false;
        }
    }

    @Override
    public boolean release(String key, String val) {
        if (Objects.isNull(singleRedissonClient)) {
            log.error("RedissonSingleLock - release singleRedissonClient is null");
            return false;
        }
        try {
            RLock lock = singleRedissonClient.getLock(key);
            lock.unlock();
            log.info("Thread [{}] RedissonSingleLock - release [{}] success", Thread.currentThread().getName(), key);
            return true;

        } catch (Exception e) {
            log.error("RedissonSingleLock - release [{}] Exception:", key, e);
            return false;
        }
    }

    @Override
    public boolean tryLock(String key, String val, long expire, long timeOut, TimeUnit timeUnit) {
        if (Objects.isNull(singleRedissonClient)) {
            log.error("RedissonSingleLock - tryLock singleRedissonClient is null");
            return false;
        }
        try {
            RLock lock = singleRedissonClient.getLock(key);
            boolean result = lock.tryLock(timeOut, expire, timeUnit);
            log.info("Thread [{}] RedissonSingleLock - tryLock [{}] result = [{}]", Thread.currentThread().getName(), key, result);
            return result;
        } catch (Exception e) {
            log.error("RedissonSingleLock - tryLock [{}] Exception:", key, e);
            return false;
        }
    }
}
