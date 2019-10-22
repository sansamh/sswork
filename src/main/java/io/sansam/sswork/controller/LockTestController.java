package io.sansam.sswork.controller;

import io.sansam.sswork.common.lock.DistributedLock;
import io.sansam.sswork.common.resp.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@ResponseResult
public class LockTestController {

    @Autowired
    DistributedLock distributedLock;

    @GetMapping("/lock")
    public String lock() {
        String key = "lock-1";
        String val = "lock-val";
        long expire = 100;
        String lock = distributedLock.lock(key, val, expire, expire);
        return lock;
    }
}
