package com.qg.exclusiveplug.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qg.exclusiveplug.model.DevicePowerRange;
import lombok.Data;

import java.util.List;

/**
 * @author Chen
 * time 2018-10-03 15:33:01
 * description 接收数据挖掘回应的类
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InteractBigData {
    /**
     * 用电器的状态
     * 0断电/1待机/2工作
     */
    int status;

    /**
     * 用电量
     */
    Double[] powerSums;

    /**
     * 用电量
     */
    Double powerSum;

    List<DevicePowerRange> devicePowerRange;
}