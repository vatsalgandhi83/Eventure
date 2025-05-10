package com.eventure.events.config;

import com.eventure.events.security.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")        // secure all /api/…
                .excludePathPatterns("/api/auth/**", "/api/user", "/api/events", "/api/events/{event_id}", "/api/bookEvent", "/api/cancelBooking", "api/events/byUser", "/api/events/createEvent", "/api/payment/create-payment"); // except your auth endpoints
    }

}
