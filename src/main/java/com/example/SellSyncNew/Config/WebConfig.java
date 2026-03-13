package com.example.SellSyncNew.Config;



import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;




    @Configuration
    public class WebConfig implements WebMvcConfigurer {

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {

            // registry.addResourceHandler("/uploads/**")
            //         .addResourceLocations(
            //                 "file:/C:/Users/SUPPORT2/Documents/Phoenix Code/dummy/SellSyncNew/uploads/",
            //                 "file:/C:/SellSync/uploads/"
            //         );
            String supportUploadPath = Paths.get("uploads/support").toAbsolutePath().toUri().toString();
            // Path-oda end-la slash illai na, manual-ah saerkanum
if (!supportUploadPath.endsWith("/")) {
    supportUploadPath += "/";
}
            registry.addResourceHandler("/uploads/support/**")
                    .addResourceLocations(supportUploadPath);

        }

    }


