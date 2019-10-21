package io.sansam.sswork.common.advice;

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
        if (o instanceof ErrorResult) {
            ErrorResult errorResult = (ErrorResult) o;
            return Result.failure(errorResult.getResultCode(), errorResult.getErrors());
        }
        return Result.success(o);
    }
}
