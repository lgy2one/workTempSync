package com.qg.exclusiveplug.service.impl;

import com.aliyuncs.exceptions.ClientException;
import com.qg.exclusiveplug.constant.SmsEnum;
import com.qg.exclusiveplug.constant.StatusEnum;
import com.qg.exclusiveplug.constant.UserEnum;
import com.qg.exclusiveplug.dao.ActionDeviceMapper;
import com.qg.exclusiveplug.dao.UserMapper;
import com.qg.exclusiveplug.dtos.Data;
import com.qg.exclusiveplug.dtos.InteractionData;
import com.qg.exclusiveplug.dtos.ResponseData;
import com.qg.exclusiveplug.model.User;
import com.qg.exclusiveplug.model.UserDeviceInfo;
import com.qg.exclusiveplug.service.UserService;
import com.qg.exclusiveplug.util.DigestUtil;
import com.qg.exclusiveplug.util.FormatMatchingUtil;
import com.qg.exclusiveplug.util.SmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author HuaChen
 * time:2018年11月5日22:50:36
 * describe:用户业务逻辑类
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ActionDeviceMapper actionDeviceMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 增加新的用户
     *
     * @param interactionData 用户信息
     * @return 是否成功
     */
    @Override
    public ResponseData register(InteractionData interactionData) {
        ResponseData responseData = new ResponseData();
        String userPhone = interactionData.getUser().getUserPhone();
        String userPassword = interactionData.getUser().getUserPassword();
        String checkCode = interactionData.getCheckCodeKey();
        log.info("注册用户手机号-->{}", userPhone);
        // 判空
        if (null != userPhone && null != userPassword && FormatMatchingUtil.isPhoneLegal(userPhone) && null != checkCode
                && !checkCode.equals("") && FormatMatchingUtil.isCheckCode(checkCode)
                && FormatMatchingUtil.isPassword(userPassword)) {
            User user = userMapper.getUserByPhone(userPhone);

            // 用户未注册
            if (null == user) {
                String code = redisTemplate.opsForValue().get(userPhone);
//                String code = checkCodeMap.get(userPhone);
                log.info("前端验证码-->{},后台验证码-->{}", checkCode, code);

                // 存在验证码
                if (null != code && !code.equals("")) {
                    String[] spilt = code.split(":");

                    // 验证码未过时且符合条件
                    if (spilt[0].equals(UserEnum.REGISTER.getStatus()) && spilt[1].equals(checkCode)) {

                        // DM5加密
                        interactionData.getUser().setUserPassword(DigestUtil.digestPassword(userPassword));
                        // 添加用户
                        userMapper.addUser(interactionData.getUser());
                        responseData.setStatus(StatusEnum.NORMAL.getStatus());
                    } else {
                        responseData.setStatus(StatusEnum.USER_CHECKCODEERROR.getStatus());
                    }
                    redisTemplate.delete(userPhone);
//                    checkCodeMap.remove(userPhone);
                } else {
                    log.info("验证码参数错误");
                    responseData.setStatus(StatusEnum.RUN_ERROR.getStatus());
                }
            } else {
                log.info("用户已被注册");
                responseData.setStatus(StatusEnum.USER_ISEXIST.getStatus());
            }
        } else {
            log.info("注册输入参数错误");
            responseData.setStatus(StatusEnum.PARAMETER_ERROR.getStatus());
        }
        return responseData;
    }

    /**
     * 发送验证码
     *
     * @param interactionData key判断登陆还是注册，用户手机号
     * @return 发送短信结果
     */
    @Override
    public ResponseData sendCheckCode(InteractionData interactionData) {
        ResponseData responseData = new ResponseData();
        String userPhone = interactionData.getUser().getUserPhone();
        String key = interactionData.getCheckCodeKey();
        log.info("验证码形式-->{}, 用户手机号-->{}", key, userPhone);

        if (null != userPhone && FormatMatchingUtil.isPhoneLegal(userPhone)) {
            String checkCode = produceCode(4);

            User user = userMapper.getUserByPhone(userPhone);
            switch (key) {
                // 注册
                case "REGISTER": {
                    if (null == user && key.equals(UserEnum.REGISTER.getStatus())) {
                        responseData = doCheckCode(userPhone, key, checkCode);
                    } else {
                        responseData.setStatus(StatusEnum.USER_ISEXIST.getStatus());
                    }
                    break;
                }
                // 登陆
                case "LOGIN": {
                    if (null != user && key.equals(UserEnum.LOGIN.getStatus())) {
                        responseData = doCheckCode(userPhone, key, checkCode);
                    } else {
                        responseData.setStatus(StatusEnum.USER_ISNOEXIST.getStatus());
                    }
                    break;
                }
                // 不存在情况
                default: {
                    responseData.setStatus(StatusEnum.PARAMETER_ERROR.getStatus());
                    break;
                }
            }
        } else {
            log.info("发送短信手机号码不符合规则-->{}", userPhone);
            responseData.setStatus(StatusEnum.PARAMETER_ERROR.getStatus());
        }
        return responseData;
    }

    /**
     * 用户账号登陆
     *
     * @param interactionData 用户电话号码以及密码
     * @param httpSession     保存会话
     * @return 登陆结果
     */
    @Override
    public ResponseData loginNormal(InteractionData interactionData, HttpSession httpSession) {
        ResponseData responseData = new ResponseData();
        String userPhone = interactionData.getUser().getUserPhone();
        String userPassword = interactionData.getUser().getUserPassword();

        if (null != userPhone && null != userPassword && FormatMatchingUtil.isPhoneLegal(userPhone)
                && FormatMatchingUtil.isPassword(userPassword)) {
            User user = userMapper.getUserByAccount(userPhone, DigestUtil.digestPassword(userPassword));

            if (null != user) {
                log.info("账号--{}--登陆成功", userPhone);
                responseData = doLogin(user, httpSession);
            } else {
                log.info("账号--{}--用户账号或密码错误", userPhone);
                responseData.setStatus(StatusEnum.USER_ACCOUNTERROR.getStatus());
            }
        } else {
            log.info("账号登陆参数错误");
            responseData.setStatus(StatusEnum.PARAMETER_ERROR.getStatus());
        }
        return responseData;
    }

    /**
     * 用户短信登陆
     *
     * @param interactionData 用户电话号码
     * @param httpSession     保存会话
     * @return 登陆结果
     */
    @Override
    public ResponseData loginSms(InteractionData interactionData, HttpSession httpSession) {
        ResponseData responseData = new ResponseData();
        String userPhone = interactionData.getUser().getUserPhone();
        String checkCode = interactionData.getCheckCodeKey();
        log.info("登陆手机号码-->{}", userPhone);

        if (null != userPhone && FormatMatchingUtil.isPhoneLegal(userPhone)) {
            User user = userMapper.getUserByPhone(userPhone);

            if (null != user) {
                String code = redisTemplate.opsForValue().get(userPhone);
//                String code = checkCodeMap.get(userPhone);

                // 存在验证码
                if (null != checkCode && !checkCode.equals("") && FormatMatchingUtil.isCheckCode(checkCode)
                        && null != code && !code.equals("")) {
                    String[] spilt = code.split(":");

                    // 验证码未过时且符合条件
                    if (spilt[0].equals(UserEnum.LOGIN.getStatus()) && spilt[1].equals(checkCode)) {

                        responseData = doLogin(user, httpSession);
                    } else {
                        log.info("验证码错误或过时");
                        responseData.setStatus(StatusEnum.USER_CHECKCODEERROR.getStatus());
                    }
                } else {
                    log.info("后台验证码错误");
                    responseData.setStatus(StatusEnum.RUN_ERROR.getStatus());
                }
            } else {
                responseData.setStatus(StatusEnum.USER_ISNOEXIST.getStatus());
            }
        } else {
            responseData.setStatus(StatusEnum.PARAMETER_ERROR.getStatus());
        }
        return responseData;
    }

    /**
     * 发送验证码及存储验证码
     *
     * @param userPhone 电话号码
     * @param key       登陆或注册关键字
     * @param checkCode 验证码
     * @return
     */
    private ResponseData doCheckCode(String userPhone, String key, String checkCode) {
        ResponseData responseData = new ResponseData();

        // 存储验证码
        redisTemplate.opsForValue().set(userPhone, key + ":" + checkCode);
        redisTemplate.expire(userPhone, 5, TimeUnit.MINUTES);
//        checkCodeMap.put(userPhone, key + ":" + checkCode + ":" + System.currentTimeMillis());
        log.info("验证码-->{}", checkCode);
        try {
            SmsUtil.sendSms(userPhone, SmsEnum.USER_ACTION.getTemplateCode(),
                    "{\"code\":\"" + checkCode + "\"}");
            responseData.setStatus(StatusEnum.NORMAL.getStatus());
        } catch (ClientException e) {
            log.info("短信发送失败");
            responseData.setStatus(StatusEnum.RUN_ERROR.getStatus());
            e.printStackTrace();
        }

        return responseData;
    }

    private ResponseData doLogin(User user, HttpSession httpSession) {
        ResponseData responseData = new ResponseData();

        // 得到用户所有的端口以及权限
        List<UserDeviceInfo> userDeviceInfoList = actionDeviceMapper.listUserDeviceIndex(user.getUserId());
        Map<Integer, Integer> indexPrivilegeMap = new HashMap<>();
        for (UserDeviceInfo userDeviceInfo : userDeviceInfoList) {
            indexPrivilegeMap.put(userDeviceInfo.getDeviceIndex(), userDeviceInfo.getUserPrivilege());
        }
        user.setIndexPrivilegeMap(indexPrivilegeMap);

        // 注入session
        httpSession.setAttribute("user", user);
        httpSession.setAttribute("userinfo", user.getUserPhone());
        System.err.print(httpSession.getId());

        // 返回用户所有的端口以及权限
        Data data = new Data();
        data.setUser(User.builder().indexPrivilegeMap(indexPrivilegeMap).build());
        responseData.setStatus(StatusEnum.NORMAL.getStatus());
        responseData.setData(data);

        return responseData;
    }

    private String produceCode(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }
}
