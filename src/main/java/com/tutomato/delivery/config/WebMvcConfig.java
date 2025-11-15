package com.tutomato.delivery.config;

import com.tutomato.delivery.common.resolver.FromAuthHeaderArgumentResolver;
import com.tutomato.delivery.domain.authentication.JwtTokenProvider;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final FromAuthHeaderArgumentResolver fromAuthHeaderArgumentResolver;

    public WebMvcConfig(FromAuthHeaderArgumentResolver fromAuthHeaderArgumentResolver) {
        this.fromAuthHeaderArgumentResolver = fromAuthHeaderArgumentResolver;
    }


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(fromAuthHeaderArgumentResolver);
    }
}
