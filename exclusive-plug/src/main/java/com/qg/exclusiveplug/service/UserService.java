package com.qg.exclusiveplug.service;

import com.qg.exclusiveplug.dtos.InteractionData;
import com.qg.exclusiveplug.dtos.ResponseData;

import javax.servlet.http.HttpSession;

/**
 * @author HuaChen
 * time:2018年11月5日22:50:36
 * describe:用户业务逻辑类
 */
public interface UserService {
    /**
     * 增加新的用户
     * @param data 用户信息
     * @return 是否成功
     */
    ResponseData register(InteractionData data);

    /**
     * 发送验证码
     * @param data key判断登陆还是注册，用户手机号
     * @return 发送短信结果
     */
    ResponseData sendCheckCode(InteractionData data);

    /**
     * 用户账号登陆
     * @param interactionData 用户电话号码以及密码
     * @param httpSession 保存会话
     * @return 登陆结果
     */
    ResponseData loginNormal(InteractionData interactionData, HttpSession httpSession);

    /**
     * 用户短信登陆
     * @param interactionData 用户电话号码
     * @param httpSession 保存会话
     * @return 登陆结果
     */
    ResponseData loginSms(InteractionData interactionData, HttpSession httpSession);
}
