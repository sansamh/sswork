package io.sansam.sswork.common.resp;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ResponseResult {

    /**
     * 使用此注解的类或者方法，方法返回值会被包装成Result对象
     */
}
