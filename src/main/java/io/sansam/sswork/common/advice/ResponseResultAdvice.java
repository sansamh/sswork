package io.sansam.sswork.common.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.sansam.sswork.common.constant.CommonConstant;
import io.sansam.sswork.common.resp.ErrorResult;
import io.sansam.sswork.common.resp.ResponseResult;
import io.sansam.sswork.common.resp.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

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

    static ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        // 判断是否包含 需要包装返回值 的标记，没有直接返回，有则重写
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        final HttpServletRequest request = attributes.getRequest();
        final ResponseResult responseResult = (ResponseResult) request.getAttribute(CommonConstant.RESPONSE_RESULT_ANN);

        return responseResult != null;
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
