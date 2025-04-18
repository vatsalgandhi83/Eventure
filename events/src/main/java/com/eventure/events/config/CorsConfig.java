package com.eventure.events.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    // @Value("${app.cors.allowed-origins}")
    // private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000")
            .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);
    }
}

