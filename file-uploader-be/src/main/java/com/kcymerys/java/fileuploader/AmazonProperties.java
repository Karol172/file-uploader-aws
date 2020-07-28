package com.kcymerys.java.fileuploader;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "amazon-properties")
@Setter
@Getter
public class AmazonProperties {

    private String bucketName;

}
