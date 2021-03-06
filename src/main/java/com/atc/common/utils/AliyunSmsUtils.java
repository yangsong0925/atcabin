package com.atc.common.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AliyunSmsUtils {


    //产品名称:云通信短信API产品,开发者无需替换
    static final String product         = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain          = "dysmsapi.aliyuncs.com";
    //填写自己的appKey
    static final String accessKeyId     = GlobalConfigUtils.AliAccessKeyId;
    static final String accessKeySecret = GlobalConfigUtils.AliAccessKeySecret;


    /**
     * 发送短信验证码
     *
     * @return
     * @throws ClientException
     * @throws ServerException
     */
    public static SendSmsResponse sendMsg(String phoneNumber, String code) throws ServerException, ClientException {

        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phoneNumber);
        request.setSignName(GlobalConfigUtils.AliMsgSignName);
        request.setTemplateCode(GlobalConfigUtils.AliTemplateCode);
        request.setTemplateParam("{\"code\":" + "\"" + code + "\"}");

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
//        request.setOutId("yourOutId");

        log.info(" AliyunSmsUtils sendMsg param phoneNumber:" + phoneNumber + " code:" + code);
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        log.info(" AliyunSmsUtils sendMsg result code:" + sendSmsResponse.getCode() + " message:" + sendSmsResponse.getMessage());
        return sendSmsResponse;
    }

    /**
     * 发送短信验证码
     *
     * @return
     * @throws ClientException
     * @throws ServerException
     */
    public static SendSmsResponse sendMsg(String phoneNumber) throws ServerException, ClientException {

        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phoneNumber);
        request.setSignName(GlobalConfigUtils.AliMsgSignName);
        request.setTemplateCode(GlobalConfigUtils.AliTemplate);
//        request.setTemplateParam("{\"code\":" + "\"" + code + "\"}");

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
//        request.setOutId("yourOutId");

        log.info(" AliyunSmsUtils sendMsg param phoneNumber:" + phoneNumber);
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        log.info(" AliyunSmsUtils sendMsg result code:" + sendSmsResponse.getCode() + " message:" + sendSmsResponse.getMessage());
        return sendSmsResponse;
    }

}