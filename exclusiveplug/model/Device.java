package com.qg.exclusiveplug.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author WilderGao
 * time 2018-09-23 09:53
 * motto : everything is no in vain
 * description 电器设备实体类
 */
@Data
@AllArgsConstructor
public class Device implements Serializable {
    /**
     * 设备Id
     */
    private int id;

    /**
     * 排插串口号
     */
    private int index;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 电流
     */
    private double current;

    /**
     * 电压
     */
    private double voltage;

    /**
     * 功率
     */

    private double power;

    /**
     * 功率系数
     */
    private double powerFactor;

    /**
     * 频率
     */
    private double frequency;

    /**
     * 发送记录的日期
     */
    private String date;

    /**
     * 累计用电量
     */
    private double cumulativePower;

    /**
     * 用电器状态
     */
    private int status;

    /**
     * 是否开关
     */
    private int stop;

    public Device(int index, String name, double current, double voltage,
                  double power, double powerFactor, double frequency, String date, double cumulativePower, int stop) {
        this.index = index;
        this.name = name;
        this.current = current;
        this.voltage = voltage;
        this.power = power;
        this.powerFactor = powerFactor;
        this.frequency = frequency;
        this.date = date;
        this.cumulativePower = cumulativePower;
        this.stop = stop;
    }
}
