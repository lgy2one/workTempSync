package com.qg.exclusiveplug.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qg.exclusiveplug.model.*;
import lombok.Data;

import java.util.List;

/**
 * @author Chen
 * time 2018-10-03 20:32:48
 * description 接收前端发来的数据
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InteractionData {

    /**
     * 串口号
     */
    int index;

    /**
     * 时间
     */
    String time;

    /**
     * 关键词
     */
    int key;

    /**
     * 用户验证码判断
     */
    String checkCodeKey;

    /**
     * 用户信息
     */
    User user;
    /**
     * 用户设备关联信息
     */
    UserDeviceInfo userDeviceInfo;

    /**
     * 设备信息
     */
    DeviceInfo deviceInfo;

    /**
     * 设备UUID
     */
    DeviceUuid deviceUuid;

    /**
     * uuid
     */
    String uuid;

    /**
     * 设备名字
     */
    String machineName;

    String name;

    Timing timing;

    List<Timing> timingList;
}
