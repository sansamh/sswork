package io.sansam.sswork.common.lock;

import java.util.concurrent.TimeUnit;

public interface DistributedLock {

    /**
     * 获取锁
     *
     * @param key      加锁的key
     * @param val      加锁的value
     * @param expire   key过期时间
     * @param timeUnit 时间取值
     * @return
     */
    boolean lock(String key, String val, long expire, TimeUnit timeUnit);

    /**
     * 释放锁
     *
     * @param key 释放锁的key
     * @param val 释放锁的value
     * @return
     */
    boolean release(String key, String val);

    /**
     * 获取锁
     *
     * @param key      加锁的key
     * @param val      加锁的value
     * @param expire   key过期时间
     * @param timeOut  获取锁超时时间
     * @param timeUnit 时间取值
     * @return
     */
    boolean tryLock(String key, String val, long expire, long timeOut, TimeUnit timeUnit);
}
