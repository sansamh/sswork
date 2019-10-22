package io.sansam.sswork.controller;

import io.sansam.sswork.common.resp.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * RedisSessionTestController
 * </p>
 *
 * @author houcb
 * @since 2019-10-22 15:09
 */
@RestController
@Slf4j
@ResponseResult
public class RedisSessionTestController {

    @Value("${server.port}")
    private Integer port;

    private String key = "userName";

    @PostMapping("/session/{name}")
    public String setSession(@PathVariable(name = "name") String uNmae, HttpServletRequest request) {
        final HttpSession session = request.getSession();
        Object attribute = session.getAttribute(key);
        if (Objects.isNull(attribute)) {
            log.info(port + "获取userName失败，现在设置userName为 " + uNmae);
            session.setAttribute(key, uNmae);
        } else {
            log.info(port + "获取userName成功" + attribute);
        }
        return "ok";
    }

    @GetMapping("/session/{name}")
    public String getSession(@PathVariable(name = "name") String uNmae, HttpServletRequest request) {
        final HttpSession session = request.getSession();
        Object attribute = Optional.ofNullable(session.getAttribute(key)).orElse("");
        log.info(port + "获取userName成功" + attribute);
        return attribute.toString();
    }
}
