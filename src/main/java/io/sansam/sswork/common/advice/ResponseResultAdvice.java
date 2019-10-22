package io.sansam.sswork.common.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.sansam.sswork.common.constant.CommonConstant;
import io.sansam.sswork.common.resp.ErrorResult;
import io.sansam.sswork.common.resp.ResponseResult;
import io.sansam.sswork.common.resp.Result;
import io.sansam.sswork.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;

/**
 * <p>
 * ResponseResultAdvice
 * </p>
 *
 * @author houcb
 * @since 2019-10-21 15:42
 */
@Slf4j
@ControllerAdvice
public class ResponseResultAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    RedisUtil redisUtil;


    static ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        // 先判断类中是否包含注解
        final Class<?> declaringClass = methodParameter.getDeclaringClass();
        String clzName = declaringClass.getName();

        boolean hasKey = redisUtil.sHasKey(CommonConstant.RESPONSE_RESULT_CLASS, clzName);
        if (hasKey) {
            return true;
        }

        boolean annoPresent = declaringClass.isAnnotationPresent(ResponseResult.class);
        if (annoPresent) {
            redisUtil.sSetForTimes(CommonConstant.RESPONSE_RESULT_CLASS, clzName, 3);
            return true;
        }

        // 处理方法
        final Method method = methodParameter.getMethod();
        String methodName = method.getName();
        String key = clzName + CommonConstant.AND + methodName;

        hasKey = redisUtil.sHasKey(CommonConstant.RESPONSE_RESULT_METHOD, key);
        if (hasKey) {
            return true;
        }

        annoPresent = method.isAnnotationPresent(ResponseResult.class);
        if (annoPresent) {
            redisUtil.sSetForTimes(CommonConstant.RESPONSE_RESULT_METHOD, key, 3);
            return true;
        }

        return false;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        log.info("进入ResponseResultAdvice - beforeBodyWrite，重写返回值");
        // 当此方法出现异常，会包装成RestExceptionAdvice的Result.failure，再进入到此方法直接返回
        if (o instanceof Result) {
            return o;
        }
        // 业务错误包装成ErrorResult
        if (o instanceof ErrorResult) {
            ErrorResult errorResult = (ErrorResult) o;
            return Result.failure(errorResult.getResultCode(), errorResult.getErrors());
        }
        // 业务返回字符串类型数据 包装成Json String
        if (o instanceof String) {
            try {
                return mapper.writeValueAsString(Result.success(o));
            } catch (JsonProcessingException e) {
                log.error("ResponseResultAdvice - beforeBodyWrite 返回值转json出现异常，待转换值为 {}", o);
                log.error("ResponseResultAdvice - beforeBodyWrite 返回值转json出现异常", e);
            }
        }
        return Result.success(o);
    }
}
