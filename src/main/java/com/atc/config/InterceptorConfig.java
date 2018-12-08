package com.atc.config;

import com.atc.filter.RequestInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestInterceptor())
                .addPathPatterns("/api/operate/**","/api/operationLog/**","/api/map/**","/api/smsCode/**","/api/checkSmsCode/**")
                .excludePathPatterns("/api/login/**","/api/register/**","/swagger-ui/**");
    }


}
