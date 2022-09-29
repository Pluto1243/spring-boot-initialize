package com.wj.boot.config.log;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("log-config")
public class LogConfig {

    private Boolean enable;

    private Boolean mqEnable;

}
