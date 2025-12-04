package com.github.stoynko.easydoc.media.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class CloudinaryConfiguration {

    @Bean
    Cloudinary cloudinary(Environment env) {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", env.getRequiredProperty("cloudinary.cloud-name"),
                "api_key",    env.getRequiredProperty("cloudinary.api-key"),
                "api_secret", env.getRequiredProperty("cloudinary.api-secret")
        ));
    }
}
