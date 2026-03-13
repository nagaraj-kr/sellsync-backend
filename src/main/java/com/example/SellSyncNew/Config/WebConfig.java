package com.example.SellSyncNew.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Only use dynamic paths for Railway Linux environment
        String supportUploadPath = Paths.get("uploads/support").toAbsolutePath().toUri().toString();
        
        registry.addResourceHandler("/uploads/support/**")
                .addResourceLocations(supportUploadPath.endsWith("/") ? supportUploadPath : supportUploadPath + "/");
    }
}
