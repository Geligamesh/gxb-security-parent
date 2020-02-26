package com.gxb.security.authentication.code;

import com.gxb.security.authentication.CustomAuthenticationFailHandler;
import com.gxb.security.authentication.exception.ValidateCodeException;
import com.gxb.security.controller.CustomLoginController;
import com.gxb.security.properties.SecurityProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码校验过滤器 所有请求之前被调用一次
 */
@Component("imageCodeValidateFilter")
public class ImageCodeValidateFilter extends OncePerRequestFilter {

    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private CustomAuthenticationFailHandler customAuthenticationFailHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        //如果是post方式的登录请求，则校验输入的验证码是否正确
        if (securityProperties.getAuthentication().getLoginProcessingUrl().equals(request.getRequestURI())
        && "post".equalsIgnoreCase(request.getMethod())) {
            try {
                validate(request);
            } catch (AuthenticationException e) {
                //交给茭白处理器进行处理异常
                customAuthenticationFailHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        //放行请求
        filterChain.doFilter(request, response);
    }

    private void validate(HttpServletRequest request) {
        //先获取session中的验证码
        String sessionCode = (String) request.getSession().getAttribute(CustomLoginController.SESSION_KEY);
        //获取用户获取的验证码
        String code = request.getParameter("code");
        //判断是否正确
        if (StringUtils.isBlank(code)) {
            throw new ValidateCodeException("验证码不能为空");
        }

        if (!code.equalsIgnoreCase(sessionCode)) {
            throw new ValidateCodeException("验证码输入错误");
        }
    }
}
