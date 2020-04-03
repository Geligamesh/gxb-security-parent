package com.gxb.security.authorize;

import com.gxb.security.authentication.authorize.AuthorizeConfigureProvider;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * 关于系统管理模块的授权模式
 */
@Component
public class SystemAuthorizeConfigureProvider implements AuthorizeConfigureProvider {

    @Override
    public void configure(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        // config.antMatchers("/user").hasAuthority("sys:user")
        //         .antMatchers(HttpMethod.GET,"/role").hasAuthority("sys:role")
        //         .antMatchers(HttpMethod.GET,"/permission")
        //         .access("hasAuthority('sys:permission') or hasAnyRole('ADMIN','ROOT')")
        //         ;
                // .antMatchers("/user").hasRole("ADMIN")
                // .antMatchers("/user").hasAnyRole("ADMIN","ROOT")
                // .anyRequest().authenticated();//所有访问该应用的http请求都要通过身份认证才  可以访问
    }
}
