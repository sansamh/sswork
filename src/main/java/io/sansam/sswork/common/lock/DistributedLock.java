package io.sansam.sswork.common.lock;

public interface DistributedLock {

    /**
     * 获取锁
     *
     * @param key     加锁的key
     * @param val     加锁的value
     * @param expire  key过期时间
     * @param timeOut 获取锁的超时时间
     * @return
     */
    String lock(String key, String val, long expire, long timeOut);

    /**
     * 释放锁
     *
     * @param key 释放锁的key
     * @return
     */
    boolean release(String key);
}
