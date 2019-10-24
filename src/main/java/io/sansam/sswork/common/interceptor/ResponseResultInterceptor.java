package io.sansam.sswork.common.interceptor;

import io.sansam.sswork.common.constant.CommonConstant;
import io.sansam.sswork.common.resp.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * <p>
 * ResponseResultInterceptor
 * </p>
 *
 * @author houcb
 * @since 2019-10-21 15:33
 */
@Slf4j
@Component
public class ResponseResultInterceptor implements HandlerInterceptor {

    /**
     * 请求拦截器，由于使用redis缓存判断是否需要包装返回值，所以此方法没用了
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            final HandlerMethod handlerMethod = (HandlerMethod) handler;
            final Class<?> beanType = handlerMethod.getBeanType();
            final Method method = handlerMethod.getMethod();
            if (beanType.isAnnotationPresent(ResponseResult.class)) {
                request.setAttribute(CommonConstant.RESPONSE_RESULT_ANN, beanType.getAnnotation(ResponseResult.class));
            } else if (method.isAnnotationPresent(ResponseResult.class)) {
                request.setAttribute(CommonConstant.RESPONSE_RESULT_ANN, method.getAnnotation(ResponseResult.class));
            }
        }
        return true;
    }
}
