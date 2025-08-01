package com.ght666.aiTools.config;

import com.ght666.aiTools.interceptor.UserAuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 1012ght
 */
@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    private UserAuthInterceptor userAuthInterceptor;
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //开发环境 允许所有来源 运行五种http方法 允许所有请求头 允许前端访问这个响应头
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Content-Disposition");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userAuthInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/user/login", "/api/user/register");
    }
}