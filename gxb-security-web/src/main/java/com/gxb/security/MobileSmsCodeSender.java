package com.gxb.security;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.gxb.security.authentication.mobile.SmsSend;
import com.gxb.security.properties.SmsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: 梦学谷 www.mengxuegu.com
 */
@Slf4j
@Component
public class MobileSmsCodeSender implements SmsSend {

    @Autowired
    private SmsProperties smsProperties;

    @Override
    public boolean sendSms(String mobile, String content) {
        log.info("Web应用新的短信验证码接口---向手机号"+mobile+"发送了验证码为：" + content);
        //发送短信
        DefaultProfile profile = DefaultProfile.getProfile(
                smsProperties.getRegionId(),
                smsProperties.getAccessKeyId(),
                smsProperties.getAccessSecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(smsProperties.getSysDomain());
        request.setVersion(smsProperties.getSysVersion());
        request.setAction(smsProperties.getSysAction());
        request.putQueryParameter("RegionId", smsProperties.getRegionId());
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", smsProperties.getSignName());
        request.putQueryParameter("TemplateCode", smsProperties.getTemplateCode());
        request.putQueryParameter("TemplateParam", "{\"code\":" + content + "}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            log.info(response.getData());
            log.info(response.getHttpResponse() + "");
            log.info(response.getHttpStatus() + "");
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }
}
