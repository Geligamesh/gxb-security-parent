package com.gxb.security.authentication.session;

import com.gxb.base.result.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 当session失效的处理逻辑
 */
@Slf4j
public class CustomInvalidSessionStrategy implements InvalidSessionStrategy {

    private SessionRegistry sessionRegistry;

    public CustomInvalidSessionStrategy(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 要将浏览器中的cookiezhong 的 JSESSIONID删除
        log.info("getSession().getId():{}" ,request.getSession().getId());
        log.info("getSession().getRequestedSessionId():{}", request.getRequestedSessionId());
        sessionRegistry.removeSessionInformation(request.getRequestedSessionId());

        this.cancelCookie(request,response);
        JsonResult jsonResult = new JsonResult().build(HttpStatus.UNAUTHORIZED.value(),"登录已经超时,请重新登录");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(jsonResult.toJsonString());
    }

    protected void cancelCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setMaxAge(0);
        cookie.setPath(getCookiePath(request));
        response.addCookie(cookie);
    }

    private String getCookiePath(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        return contextPath.length() > 0 ? contextPath : "/";
    }
}
