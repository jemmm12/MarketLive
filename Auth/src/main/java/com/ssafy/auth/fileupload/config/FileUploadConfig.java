package com.ssafy.auth.fileupload.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileUploadConfig implements WebMvcConfigurer {

    private final String uploadurl;

    public FileUploadConfig(@Value("${custom.path.uploadurl}") String uploadurl) {
        this.uploadurl = uploadurl;
    }


}
