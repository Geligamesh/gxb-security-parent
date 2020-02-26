package com.gxb.security.authentication;

import com.alibaba.fastjson.JSON;
import com.gxb.base.result.JsonResult;
import com.gxb.security.properties.LoginResponseType;
import com.gxb.security.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证成功处理器
 *
 * 1.决定响应json还是跳转页面 ，或者认证成功后进行其他处理
 */
@Slf4j
@Component("customAuthenticationSuccessHandler")
// public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //认证成功后，响应json字符串
        if (LoginResponseType.JSON.equals(securityProperties.getAuthentication().getLoginType())) {
            JsonResult result = JsonResult.ok("认证成功");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(result.toJsonString());
        }else {
            //重定向到上次请求的地址上，引发跳转到认证页面的地址
            log.info("authentication:{}", JSON.toJSONString(authentication));
            super.onAuthenticationSuccess(request, response, authentication);
        }

    }
}
