package com.qg.exclusiveplug.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author Chen
 * time 2018-10-02 17:26:30
 * description 向数据挖掘发送请求的类
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestData<T> {

    /**
     * 各类信息
     */
    T data;

    /**
     * 列表
     */
    List<T> list;

    /**
     * 用电量
     */
//    Double[] powerSums;

    List<Double> powerSums;

    String name;

    String newName;
}
