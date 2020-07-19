package com.qg.exclusiveplug.dao;

import com.qg.exclusiveplug.model.User;
import com.qg.exclusiveplug.model.UserDeviceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    /**
     * 增加用户信息
     */
    int addUser(User user);

    /**
     * 通过手机号得到用户信息
     * @param userPhone 用户手机号码
     * @return 是否成功
     */
    User getUserByPhone(String userPhone);

    /**
     * 通过手机号得到用户信息
     * @param userPhone 用户手机号码
     * @param userPassword 用户密码
     * @return 是否成功
     */
    User getUserByAccount(@Param("userPhone") String userPhone, @Param("userPassword") String userPassword);

    /**
     * 通过用户ID得到用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    User getUserByUserId(int userId);

    /**
     * 根据用户ID查询用户与设备的关联信息
     * @param userId 用户ID
     * @return 查询结果
     */
    List<UserDeviceInfo> getUserDeviceInfoByUserId(int userId);


}
