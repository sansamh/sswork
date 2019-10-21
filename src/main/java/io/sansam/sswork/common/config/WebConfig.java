package io.sansam.sswork.common.config;

import io.sansam.sswork.common.interceptor.ResponseResultInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * <p>
 * WebConfig
 * </p>
 *
 * @author houcb
 * @since 2019-10-21 16:36
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    ResponseResultInterceptor responseResultInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(responseResultInterceptor).addPathPatterns("/**");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // ResponseResultAdvice重写了返回值后，如果controller返回String，则会报错对象无法转为String
        // 1. 可以在ResponseResultAdvice里判断如果返回值为String则替换为JSON.toJSONString
        // 2. 因为在所有的 HttpMessageConverter 实例集合中，StringHttpMessageConverter 要比其它的 Converter 排得靠前一些。
        // 我们需要将处理 Object 类型的 HttpMessageConverter 放得靠前一些，可以在 Configuration 类中完成编码
        converters.add(0, new MappingJackson2HttpMessageConverter());
    }
}
