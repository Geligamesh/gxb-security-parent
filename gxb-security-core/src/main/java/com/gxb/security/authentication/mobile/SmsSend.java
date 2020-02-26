package com.gxb.security.authentication.mobile;

public interface SmsSend {

    /**
     * 短信发送统一接口
     * @param mobile
     * @param content
     * @return
     */
    boolean sendSms(String mobile,String content);
}
