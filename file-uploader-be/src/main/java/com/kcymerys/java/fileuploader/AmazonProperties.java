package com.kcymerys.java.fileuploader;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;

@ConfigurationProperties(prefix = "amazon-properties")
@Setter
@Getter
@Profile("aws")
public class AmazonProperties {

    private String bucketName;

}
