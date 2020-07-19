package com.qg.exclusiveplug.controller;

import com.qg.exclusiveplug.dtos.InteractionData;
import com.qg.exclusiveplug.dtos.ResponseData;
import com.qg.exclusiveplug.service.ActionDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author HuaChen
 * time:2018年11月6日22:17:12
 * description:用户操控设备控制类
 */
@RestController
@Slf4j
@CrossOrigin
@RequestMapping("actiondevice")
public class ActionDeviceController {
    @Autowired
    private ActionDeviceService actionDeviceService;

    /**
     * 操控设备的开关
     * @param interactionData 设备端口以及开关信息
     * @return 处理结果
     */
    @RequestMapping("/controller")
    public ResponseData controller(@RequestBody InteractionData interactionData) {
        return actionDeviceService.controller(interactionData);
    }

    @PostMapping("adddevice")
    public ResponseData addDevice(@RequestBody InteractionData interactionData, HttpSession httpSession) {
        return actionDeviceService.addDevice(interactionData, httpSession);
    }

    @PostMapping("adddeviceinfo")
    public ResponseData addDeviceInfo(@RequestBody InteractionData interactionData, HttpSession httpSession) {
        return actionDeviceService.addDeviceInfo(interactionData, httpSession);
    }

    @PostMapping("querydeviceinfo")
    public ResponseData queryDeviceInfo(@RequestBody InteractionData interactionData, HttpSession httpSession) {
        return actionDeviceService.queryDeviceInfo(interactionData, httpSession);
    }

    @PostMapping("updatedeviceinfo")
    public ResponseData updateDeviceInfo(@RequestBody InteractionData interactionData, HttpSession httpSession) {
        return actionDeviceService.updateDeviceInfo(interactionData, httpSession);
    }

    @PostMapping("updatedevicename")
    public ResponseData updateDeviceName(@RequestBody InteractionData interactionData, HttpSession httpSession) {
        return actionDeviceService.updateDeviceName(interactionData, httpSession);
    }

    @PostMapping("deldevice")
    public ResponseData delDevice(@RequestBody InteractionData interactionData, HttpSession httpSession) {
        return actionDeviceService.delDevice(interactionData, httpSession);
    }

    @PostMapping("querydevicelog")
    public ResponseData queryDeviceLog(@RequestBody InteractionData interactionData) {
        return actionDeviceService.queryDeviceLog(interactionData);
    }

    @PostMapping("timing")
    public ResponseData timing(@RequestBody InteractionData interactionData, HttpSession httpSession) {
        return actionDeviceService.timing(interactionData, httpSession);
    }

    @GetMapping("listtiming")
    public ResponseData listTiming(HttpSession httpSession) {
        return actionDeviceService.listTiming(httpSession);
    }

    @PostMapping("deltiming")
    public ResponseData delTiming(@RequestBody InteractionData data) {
        return actionDeviceService.delTiming(data);
    }

    @PostMapping("feedback")
    public ResponseData feedback(@RequestBody InteractionData data) {
        return actionDeviceService.feedback(data);
    }
}
