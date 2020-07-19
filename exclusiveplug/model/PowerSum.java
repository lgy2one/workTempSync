package com.qg.exclusiveplug.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Chen
 * time: 2018-10-05 15:00:26
 * descreption: 耗电量类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PowerSum {
    /**
     * 用电时间
     */
    private String time;

    /**
     * 耗电量
     */
    private Double powerSum;
}
