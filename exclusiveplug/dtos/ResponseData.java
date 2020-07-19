package com.qg.exclusiveplug.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Chen
 * time 2018-10-03 15:42:20
 * description 响应前端的类
 */
@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
public class ResponseData implements Serializable {
    /**
     * 响应状态码
     */
    String status;

    /**
     * 数据
     */
    Data data;
}
