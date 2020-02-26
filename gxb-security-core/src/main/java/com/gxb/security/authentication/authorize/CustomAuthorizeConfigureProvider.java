package com.gxb.security.authentication.authorize;

import com.gxb.security.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * 身份认证相关的授权配置
 */
@Component
@Order(Integer.MAX_VALUE) //值越大加载越往后
public class CustomAuthorizeConfigureProvider implements AuthorizeConfigureProvider {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void configure(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        config.antMatchers(securityProperties.getAuthentication().getLoginPage(),
                securityProperties.getAuthentication().getMobileCodeUrl(),
                securityProperties.getAuthentication().getMobilePage(),
                securityProperties.getAuthentication().getImageCodeUrl())
                .permitAll() // 放行login/page不需要认证可访问
                .anyRequest().authenticated()
        ;
    }
}
