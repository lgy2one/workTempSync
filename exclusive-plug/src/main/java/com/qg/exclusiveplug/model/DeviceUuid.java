package com.qg.exclusiveplug.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author HuaChen
 * time:2018年11月6日20:18:24
 * description:设备的出厂设置类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceUuid {
    /**
     * 设备端口号
     */
    private int deviceIndex;

    /**
     * 设备的UUID
     */
    private String uuid;

    /**
     * 设备的权限
     */
    private int devicePrivilege;
}
