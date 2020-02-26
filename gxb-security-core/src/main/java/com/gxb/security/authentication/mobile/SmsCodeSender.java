package com.gxb.security.authentication.mobile;

import lombok.extern.slf4j.Slf4j;

/**
 * 发送短信验证码，第三方的短信服务接口
 */
@Slf4j
public class SmsCodeSender implements SmsSend {

    @Override
    public boolean sendSms(String mobile, String content) {
        String sendContent = String.format("梦学谷，验证码%s, 请勿泄露给别人。", content);
        // 调用每三方发送功能的sdk
        log.info("向手机号：" + mobile + "发送的短信为：" + sendContent);
        return true;
    }
}
