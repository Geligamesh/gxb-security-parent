package com.gxb.security.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "sms")
@Component
public class SmsProperties {

    private String accessKeyId;
    private String accessSecret;
    private String sysDomain;
    private String sysAction;
    private String regionId;
    private String signName;
    private String templateCode;
    private String sysVersion;
}
