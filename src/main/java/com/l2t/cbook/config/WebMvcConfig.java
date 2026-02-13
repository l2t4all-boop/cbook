package com.l2t.cbook.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final ApiKeyInterceptor apiKeyInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiKeyInterceptor)
                .addPathPatterns("/api/v1/cbook/**")
                .excludePathPatterns(
                        "/api/v1/cbook/api-keys/**",
                        "/api/v1/cbook/greetings",
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                );
    }
}
