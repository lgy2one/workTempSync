package com.qg.exclusiveplug.constant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StatusEnum {
    /**
     * 一切正常
     */
    NORMAL("2000"),

    /**
     * 预测失败
     */
    PREDICTED_FAILED("5001"),

    /**
     * 服务器发生未知错误
     */
    RUN_ERROR("5000"),

    /**
     * 参数解析错误
     */
    PARAMETER_ERROR("4001"),

    /**
     * 用户已注册
     */
    USER_ISEXIST("4021"),

    /**
     * 用户账号或者密码错误
     */
    USER_ACCOUNTERROR("4022"),

    /**
     * 用户未注册
     */
    USER_ISNOEXIST("4023"),

    /**
     * 用户未登陆
     */
    USER_ISNOLOGIN("4030"),

    /**
     * 用户权限不足
     */
    USER_HASNOPRIVILEGE("4031"),

    /**
     * 验证码未发送或已过时
     */
    USER_CHECKCODEERROR("4024"),

    /**
     * 不存在该端口的设备
     */
    DEVICE_ISNOEXIST("4040")
    ;

    private String status;

    public String getStatus() {
        return status;
    }
}
