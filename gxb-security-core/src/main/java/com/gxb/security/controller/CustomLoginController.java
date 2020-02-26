package com.gxb.security.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
@Slf4j
public class CustomLoginController {

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    public static final String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";

    @RequestMapping("login/page")
    public String toLogin() {
        return "login";
    }

    /**
     * 获取图形验证码
     * @param request
     * @param response
     */
    @RequestMapping("code/image")
    public void imageCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1.获取验证码字符串
        String code = defaultKaptcha.createText();
        log.info("生成的验证码:{}", code);
        //2.字符串把它放到session
        request.getSession().setAttribute(SESSION_KEY, code);
        //3.获取验证码图片
        BufferedImage image = defaultKaptcha.createImage(code);
        //4.将验证码图片写出去
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(image, "jpg", outputStream);
    }
}
