package com.gxb.security.authentication.mobile;

import com.gxb.security.authentication.CustomAuthenticationFailHandler;
import com.gxb.security.authentication.exception.ValidateCodeException;
import com.gxb.security.controller.MobileLoginController;
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
 * 校验用户输入的的手机验证码是否正确
 */
@Component
public class MobileValidateFilter extends OncePerRequestFilter {

    @Autowired
    private CustomAuthenticationFailHandler customAuthenticationFailHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //判断请求是否为手机登录，且post请求
        if ("/mobile/form".equals(request.getRequestURI())
            && "post".equalsIgnoreCase(request.getMethod())) {
            try {
                validate(request);
            } catch (AuthenticationException e) {
                //交给茭白处理器进行处理异常
                customAuthenticationFailHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        //放行
        filterChain.doFilter(request, response);
    }

    private void validate(HttpServletRequest request) {
        //先获取session中的验证码
        String sessionCode = (String) request.getSession().getAttribute(MobileLoginController.SESSION_KEY);
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
