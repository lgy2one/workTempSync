package com.qg.exclusiveplug.service;

import com.qg.exclusiveplug.dtos.ResponseData;

/**
 * @author WilderGao
 * time 2018-10-04 00:44
 * motto : everything is no in vain
 * description 预测部分逻辑层
 */
public interface PredictService {
    /**
     * 预测今天的用电量
     *
     * @param time  当前时间
     * @param index 串口
     * @return 预测结果
     */
    ResponseData predictNowPowerSumService(String time, int index);
}
