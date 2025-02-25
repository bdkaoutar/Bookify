package com.kaoutar.bookify.Bookify.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Allow CORS requests from the frontend (localhost:5173)
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")  // Your frontend URL
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Allowed HTTP methods
                .allowCredentials(true);
    }
}

// this is to only allow CORS for the AuthenticationController
//import org.springframework.web.bind.annotation.CrossOrigin;
//
//@RestController
//@RequestMapping("/api/v1/auth")
//@CrossOrigin(origins = "http://localhost:5173")  // Allow CORS for this controller
//public class AuthenticationController {
//    // Your existing controller code
//}
