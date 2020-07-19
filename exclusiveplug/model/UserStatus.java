package com.qg.exclusiveplug.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author HuaChen
 * time:2018年11月10日19:59:28
 * description:用户的WebSocket状态
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserStatus {
    /**
     * 用户ID
     */
    int userId;

    /**
     * 用户所在的端口
     */
    int deviceIndex;
}
