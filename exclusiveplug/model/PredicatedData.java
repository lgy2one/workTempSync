package com.qg.exclusiveplug.model;

import lombok.Data;


/**
 * @author Cheng
 * time 2018-10-01 20:34:19
 * description 预测数据实体类
 */
@Data
public class PredicatedData {
    /**
     * 数据编号
     */
    private int id;

    /**
     * 串口号
     */
    private int index;

    /**
     * 电器名称
     */
    private String name;

    /**
     * 数据日期
     */
    private String date;

    /**
     * 每日用电总量
     */
    private double cumulativePower;

}
