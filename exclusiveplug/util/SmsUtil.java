package com.qg.exclusiveplug.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class SmsUtil {
    //产品名称:云通信短信API产品,开发者无需替换
    @Value("${aliyun.sms.product}")
    private static String product = "Dysmsapi";
    //产品域名,开发者无需替换
    @Value("${aliyun.sms.domain}")
    private static String domain = "dysmsapi.aliyuncs.com";

    @Value("${aliyun.sms.accessKeyId}")
    private static String accessKeyId = "LTAIAU2HSahyGWkS";

    @Value("${aliyun.sms.accessKeySecret}")
    private static String accessKeySecret = "OG4kUcuHwjYVuXbP3OBWurSBjb3S5s";

    @Value("${aliyun.sms.regionId}")
    private static String regionId = "cn-hangzhou";

    @Value("${aliyun.sms.endpointName}")
    private static String endpointName = "cn-hangzhou";

    @Value("${aliyun.sms.signName}")
    private static String signName = "QG智能排插";

    public static SendSmsResponse sendSms(String phoneNumber, String paramterJson, String templateCode) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint(endpointName, regionId, product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        log.info("endpointName:" + endpointName);
        log.info("regionId:" + regionId);
        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phoneNumber);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam(paramterJson);

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("yourOutId");
        log.info("短信已发送,收件人-->{}, 设置参数-->{}", phoneNumber, paramterJson);

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse smsResponse = acsClient.getAcsResponse(request);
        System.out.println(smsResponse.getCode());
        return null;
    }


    public static QuerySendDetailsResponse querySendDetails(String bizId) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber("15000000000");
        //可选-流水号
        request.setBizId(bizId);
        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(ft.format(new Date()));
        //必填-页大小
        request.setPageSize(10L);
        //必填-当前页码从1开始计数
        request.setCurrentPage(1L);

        //hint 此处可能会抛出异常，注意catch
        return acsClient.getAcsResponse(request);
    }
}