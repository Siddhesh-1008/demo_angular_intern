package com.tester_proj.usersmanagementsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    // This method configures Cross-Origin Resource Sharing (CORS) for the application
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Allow CORS requests for all API endpoints (/** means all paths)
                registry.addMapping("/**")
                        // Allow only these HTTP methods: GET, POST, PUT, DELETE
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        // Allow requests from any domain (public API access)
                        .allowedOrigins("*");
            }
        };
    }
}

//58.22

