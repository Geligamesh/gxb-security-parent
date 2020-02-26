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
@Component
@ConfigurationProperties(prefix = "gxb.security")
public class SecurityProperties {

    // 会将 mengxuegu.security.authentication 下面的值绑定到AuthenticationProperties对象上
    private AuthenticationProperties authentication;


}
