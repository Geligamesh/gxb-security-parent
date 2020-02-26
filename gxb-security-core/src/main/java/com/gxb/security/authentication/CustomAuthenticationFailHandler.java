package com.gxb.security.authentication;

import com.gxb.base.result.JsonResult;
import com.gxb.security.properties.LoginResponseType;
import com.gxb.security.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理失败认证
 */
@Slf4j
@Component("customAuthenticationFailHandler")
// public class CustomAuthenticationFailHandler implements AuthenticationFailureHandler {
public class CustomAuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private SecurityProperties securityProperties;

    /**
     *
     * @param request
     * @param response
     * @param e 认证失败后抛出的异常
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        //认证失败后，响应json字符串
        if (LoginResponseType.JSON.equals(securityProperties.getAuthentication().getLoginType())) {
            JsonResult result = JsonResult.build(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(result.toJsonString());
        }else {
            //重定向回认证页面
            // super.setDefaultFailureUrl(securityProperties.getAuthentication().getLoginPage() + "?error");
            String referer = request.getHeader("Referer");
            log.info("referer:{}", referer);
            //如果下面有值，则认为是多端登录，跳转到登录页面
            Object toAuthentication = request.getAttribute("toAuthentication");
            String lastUrl = toAuthentication != null ? securityProperties.getAuthentication().getLoginPage() :
                    StringUtils.substringBefore(referer, "?");

            log.info("上一次请求的路径:{}", lastUrl);
            super.setDefaultFailureUrl(lastUrl + "?error");
            super.onAuthenticationFailure(request, response, e);
        }
    }
}
