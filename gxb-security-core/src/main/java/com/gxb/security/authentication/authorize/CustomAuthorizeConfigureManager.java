package com.gxb.security.authentication.authorize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 授权配置统一接口，所有模块的授权配置类都要实现这个接口
 */
@Component
public class CustomAuthorizeConfigureManager implements AuthorizeConfigureManager {

    @Autowired
    private List<AuthorizeConfigureProvider> authorizeConfigureProviders;

    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        authorizeConfigureProviders.forEach(provider -> {
            provider.configure(config);
        });

    }
}
