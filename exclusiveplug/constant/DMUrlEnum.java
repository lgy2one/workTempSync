package com.qg.exclusiveplug.constant;

import lombok.AllArgsConstructor;

/**
 * 存放与数据挖掘端交互的URL
 *
 * @author Chen
 */
@AllArgsConstructor
public enum DMUrlEnum {

    /**
     * 实时设备数据
     */
    JUDGE_STATUS("http://39.98.41.126:5050/paicha/state/judge"),

    /**
     * 预测当天用电量
     */
    PREDICTED_POWERSUM("http://39.98.41.126:5050/paicha/predict/one"),

    TRAIN_ONE("http://39.98.41.126:5050/paicha/train/one");

    private String url;

    public String getDMUrl() {
        return url;
    }
}
