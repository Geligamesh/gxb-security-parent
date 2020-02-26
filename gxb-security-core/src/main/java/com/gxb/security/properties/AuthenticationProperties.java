package com.gxb.security.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationProperties {

    private String loginPage = "/login/page";
    private String loginProcessingUrl = "/login/form";
    private String usernameParameter = "name";
    private String passwordParameter = "pwd";
    private String[] staticPaths = {"/dist/**", "/modules/**", "/plugins/**"};

    // 获取图形验证码地址
    private String imageCodeUrl = "/code/image";
    // 发送手机验证码地址
    private String mobileCodeUrl = "/code/mobile";
    // 前往手机登录页面
    private String mobilePage = "/mobile/page";
    // 记住我功能有效时长
    private Integer tokenValiditySeconds = 604800;

    /**
     * 认证的响应的类型JSON/REDIRECT
     */
    private LoginResponseType loginType = LoginResponseType.REDIRECT;
}
