package com.qg.exclusiveplug.service;

import com.qg.exclusiveplug.dtos.InteractionData;
import com.qg.exclusiveplug.dtos.ResponseData;

import javax.servlet.http.HttpSession;
import java.text.ParseException;

/**
 * @author Wilder
 */
public interface QueryDeviceService {

    /**
     * 得到某天(24小时)/某月(30天)的用电量
     *
     * @param interactionData key(天/月/周)，时间
     * @return 状态码和用电量集合
     */
    ResponseData listPowerSum(InteractionData interactionData) throws ParseException;

    /**
     * 得到用户的所有端口以及权限
     * @return 用户的所有端口以及权限
     */
    ResponseData queryIndexs(HttpSession httpSession);

}
