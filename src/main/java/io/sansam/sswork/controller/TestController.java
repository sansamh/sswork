package io.sansam.sswork.controller;

import io.sansam.sswork.common.resp.ErrorResult;
import io.sansam.sswork.common.resp.ResponseResult;
import io.sansam.sswork.common.resp.ResultCode;
import io.sansam.sswork.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * <p>
 * TestController
 * </p>
 *
 * @author houcb
 * @since 2019-10-21 15:17
 */
@Controller
@Slf4j
public class TestController {

    @Autowired
    RedisUtil redisUtil;


    @GetMapping("/failure")
    @ResponseResult
    @ResponseBody
    public ErrorResult failure() {
        log.info("进入TestController - failure方法");
        return new ErrorResult(ResultCode.PARAM_NOT_EXISTS);
    }

    @GetMapping("/date")
    public Date date() {
        log.info("进入TestController - date方法");
        return new Date();
    }

    @PostMapping("/set/{1}")
    @ResponseResult
    public String redisSet(@PathVariable(name = "1") String id) {
        final boolean set = redisUtil.set(id, id + System.currentTimeMillis());
        return set ? Boolean.TRUE.toString() : Boolean.FALSE.toString();
    }

    @GetMapping("/get/{1}")
    @ResponseResult
    @ResponseBody
    public String redisGet(@PathVariable(name = "1") String id) {
        final Object o = redisUtil.get(id);
        return o.toString();
    }
}
