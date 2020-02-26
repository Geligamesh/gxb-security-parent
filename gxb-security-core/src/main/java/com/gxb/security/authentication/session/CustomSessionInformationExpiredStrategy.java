package com.gxb.security.authentication.session;

import com.gxb.security.authentication.CustomAuthenticationFailHandler;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * 当同一用户的session达到指定的数量时，会执行该类
 */
public class CustomSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

    @Autowired
    private CustomAuthenticationFailHandler customAuthenticationFailHandler;

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException {
        //获取用户名
        UserDetails userDetails = (UserDetails) event.getSessionInformation().getPrincipal();
        AuthenticationException exception = new AuthenticationServiceException(String.format("[%s] 用户在另外一台设备登录,您已经被强制下线",userDetails.getUsername()));
        try {
            event.getRequest().setAttribute("toAuthentication",true);
            customAuthenticationFailHandler.onAuthenticationFailure(event.getRequest(),
                    event.getResponse(),
                    exception);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }
}
