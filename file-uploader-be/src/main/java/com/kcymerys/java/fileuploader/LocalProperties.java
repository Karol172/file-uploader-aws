package com.kcymerys.java.fileuploader;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;

@ConfigurationProperties(prefix = "local-properties")
@Setter
@Getter
@Profile("local")
public class LocalProperties {

    private String workDir;

}
