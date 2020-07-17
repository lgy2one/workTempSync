package com.qg.exclusiveplug.service;

import com.qg.exclusiveplug.dtos.InteractionData;
import com.qg.exclusiveplug.dtos.ResponseData;

import javax.servlet.http.HttpSession;

public interface ActionDeviceService {
    /**
     * 控制用电器的开关
     *
     * @param interactionData 串口
     * @return 是否操控成功
     */
    ResponseData controller(InteractionData interactionData);

    /**
     * 增加用户设备
     *
     * @param interactionData 设备UUID
     * @return 操作结果 若成功则返回用户端口权限Map集合
     */
    ResponseData addDevice(InteractionData interactionData, HttpSession httpSession);

    /**
     * 增加用户关联设备信息
     *
     * @param interactionData 用户关联设备信息
     * @param httpSession     用户信息
     * @return 操作结果
     */
    ResponseData addDeviceInfo(InteractionData interactionData, HttpSession httpSession);

    /**
     * 查看用户关联设备信息
     *
     * @param interactionData 设备端口
     * @return 操作结果 若成功则返回端口信息
     */
    ResponseData queryDeviceInfo(InteractionData interactionData, HttpSession httpSession);

    /**
     * 修改用户关联设备信息
     *
     * @param interactionData 用户关联设备信息
     * @param httpSession     用户信息
     * @return 操作结果
     */
    ResponseData updateDeviceInfo(InteractionData interactionData, HttpSession httpSession);

    /**
     * 修改端口名字
     *
     * @param interactionData 端口号，设备新名字
     * @param httpSession     用户信息
     * @return 操作结果
     */
    ResponseData updateDeviceName(InteractionData interactionData, HttpSession httpSession);

    /**
     * 删除设备
     *
     * @param interactionData 设备串口号
     * @param httpSession     用户信息
     * @return 操作结果 若成功则返回设备操作信息
     */
    ResponseData delDevice(InteractionData interactionData, HttpSession httpSession);

    /**
     * 获取设备被系统操作日志
     *
     * @param interactionData 端口号
     * @return 日志信息
     */
    ResponseData queryDeviceLog(InteractionData interactionData);

    /**
     * 定时任务
     *
     * @param interactionData 串口号，启动或关闭，时间
     * @param httpSession     用户信息
     * @return 操作结果
     */
    ResponseData timing(InteractionData interactionData, HttpSession httpSession);

    ResponseData listTiming(HttpSession httpSession);

    ResponseData delTiming(InteractionData interactionData);

    ResponseData feedback(InteractionData data);
}
