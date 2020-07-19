package com.qg.exclusiveplug.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author HuaChen
 * time:2018年11月8日19:16:47
 * description:设备被系统操作日志
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceLog {
    /**
     * 设备端口
     */
    int deviceIndex;

    /**
     * 设备名称
     */
    String deviceName;

    /**
     * 设备被操作状态
     */
    int deviceStatus;

    /**
     * 设备被操作时间
     */
    String deviceTime;

    /**
     * 设备被操作原因
     */
    String deviceReason;
}
