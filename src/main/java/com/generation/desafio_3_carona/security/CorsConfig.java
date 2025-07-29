package com.generation.desafio_3_carona.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(
                "http://localhost:5173",
                "https://carona-nu.vercel.app",
                "https://carona-grupo-4-java-81s-projects.vercel.app",
                "https://carona-fork.vercel.app",
                "https://carona-fork-carlos-henrique-da-silva-barbosas-projects.vercel.app",
                "https://carona-fork.onrender.com",
                "https://*.vercel.app"
            )
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);
    }
}
