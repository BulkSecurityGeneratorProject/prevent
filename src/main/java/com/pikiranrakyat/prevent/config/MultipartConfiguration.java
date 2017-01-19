package com.pikiranrakyat.prevent.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

/**
 * Created by avew on 12/23/15.
 */
@Configuration
public class MultipartConfiguration {

    @Value("${spring.multipart.filesize}")
    String fileSize;

    @Value("${spring.multipart.requestsize}")
    String requestSize;


    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(fileSize);
        factory.setMaxRequestSize(requestSize);
        return factory.createMultipartConfig();
    }
}
