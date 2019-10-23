package io.sansam.sswork.controller;

import io.sansam.sswork.common.lock.DistributedLock;
import io.sansam.sswork.common.lock.RedissonSingleLock;
import io.sansam.sswork.common.resp.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@ResponseResult
public class LockTestController {

    @Autowired
    @Qualifier(value = "redisSingleLock")
    DistributedLock distributedLock;

    String key = "lock-1";
    long expire = 10000;
    String val = "lock-val";
    TimeUnit timeUnit = TimeUnit.MILLISECONDS;

    @GetMapping("/redis/single/lock")
    public String singleLock() {
        return Objects.toString(distributedLock.lock(key, val, expire, timeUnit));
    }

    @GetMapping("/redis/single/lock/release")
    public String singleRelease() {
        final boolean release = distributedLock.release(key, val);
        return Objects.toString(release);
    }

    @Autowired
    RedissonSingleLock redissonSingleLock;

    @GetMapping("/redisson/single/lock")
    public String redissonSingleLock() {


        return Objects.toString(redissonSingleLock.lock(key, val, expire, timeUnit));
    }

    @GetMapping("/redisson/single/lock/release")
    public String redissonSingleRelease() {
        final boolean release = redissonSingleLock.release(key, val);
        return Objects.toString(release);
    }

    @GetMapping("/redisson/test")
    public String redissonSingleTest() {

        final CountDownLatch countDownLatch = new CountDownLatch(5);
        try {
            for (int i = 0; i < 5; i++) {
                new Thread(() -> {
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final boolean lock = distributedLock.lock(key, val, expire, timeUnit);
                    log.info(Thread.currentThread().getId() + " 获取锁 " + lock);
                }).start();
                countDownLatch.countDown();
            }
        } catch (Exception e) {
            log.error("redisson test caught exception " + e);
        }


        return "OK";
    }

}
