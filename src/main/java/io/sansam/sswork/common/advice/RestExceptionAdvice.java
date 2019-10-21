package io.sansam.sswork.common.advice;

import io.sansam.sswork.common.resp.Result;
import io.sansam.sswork.common.resp.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p>
 * ExceptionAdvice
 * </p>
 *
 * @author houcb
 * @since 2019-10-21 15:42
 */
@Slf4j
@RestControllerAdvice
public class RestExceptionAdvice {

    /**
     * 全局异常处理，异常被捕获后，输出之前还是会走 ResponseResultAdvice
     * 此类为RestControllerAdvice，捕捉到异常之后返回值为Json字符串类型，会被ResponseResultAdvice - beforeBodyWrite 捕获到，进行返回值处理
     *
     * @param t
     * @return
     */
    @ExceptionHandler(Throwable.class)
    public Result apiExceptionHandler(Throwable t) {
        log.error("RestExceptionAdvice 抛出异常：", t);
        return Result.failure(ResultCode.SYSTEM_ERROR, t.getMessage());
    }

}
