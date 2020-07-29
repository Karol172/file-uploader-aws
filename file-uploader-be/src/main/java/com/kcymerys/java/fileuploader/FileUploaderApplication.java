package com.kcymerys.java.fileuploader;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties({AmazonProperties.class})
public class FileUploaderApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileUploaderApplication.class, args);
    }

    @Bean
    public AmazonS3 getAmazonS3Client() {
        return AmazonS3ClientBuilder.standard().build();
    }

}
