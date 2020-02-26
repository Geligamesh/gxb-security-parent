package com.gxb.security.controller;

import com.gxb.base.result.JsonResult;
import com.gxb.security.authentication.mobile.SmsSend;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class MobileLoginController {

    public static final String SESSION_KEY = "SESSION_KEY_MOBILE_KEY";

    @Autowired
    private SmsSend smsSend;

    @RequestMapping("mobile/page")
    public String toMobilePage() {
        return "login-mobile";
    }

    /**
     * 发送手机验证码
     * @return
     */
    @ResponseBody
    @GetMapping("code/mobile")
    public JsonResult smsCode(@RequestParam("mobile") String mobile, HttpServletRequest request) {
        //生成一个手机验证码
        String code = RandomStringUtils.randomNumeric(6);
        //将手机验证码保存到session中
        log.info("短信验证码:{}", code);
        request.getSession().setAttribute(SESSION_KEY, code);
        //发送验证码到用户手机上
        // smsSend.sendSms(mobile, code);
        return JsonResult.ok();
    }
}
