package com.qg.exclusiveplug.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceInfo {
    /**
     * 用电器端口
     */
    private int deviceIndex;

    /**
     * 用电器工作功率
     */
    private double deviceWorkPower;

    /**
     * 用电器待机功率
     */
    private double deviceStandbyPower;

    /**
     * 是否自动关闭
     */
    int autoClose;
}
