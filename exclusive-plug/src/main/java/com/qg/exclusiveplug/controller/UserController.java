package com.qg.exclusiveplug.controller;

import com.qg.exclusiveplug.dtos.InteractionData;
import com.qg.exclusiveplug.dtos.ResponseData;
import com.qg.exclusiveplug.service.AccessLimitService;
import com.qg.exclusiveplug.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author HuaChen
 * time:2018年11月6日10:15:15
 * description:用户控制器
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccessLimitService accessLimitService;
    /**
     * 用户注册
     *
     * @param interactionData 用户信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public ResponseData register(@RequestBody InteractionData interactionData) {
        return userService.register(interactionData);
    }

    /**
     * 发送验证码
     *
     * @param interactionData key判断登陆还是注册，用户手机号
     * @return 发送短信结果
     */
    @PostMapping("/sendcheckcode")
    public ResponseData sendCheckCode(@RequestBody InteractionData interactionData) {
        return userService.sendCheckCode(interactionData);
    }

    /**
     * 用户普通登录
     */
    @PostMapping("/loginnormal")
    public ResponseData loginNormal(HttpServletRequest httpServletRequest, @RequestBody InteractionData interactionData, HttpSession httpSession) {
        if (accessLimitService.tryAcquire()) {
            return userService.loginNormal(interactionData, httpSession);
        } else {
            return null;
        }
    }

    /**
     * 用户短信登陆
     */
    @PostMapping("/loginsms")
    public ResponseData loginSms(@RequestBody InteractionData interactionData, HttpSession httpSession) {
        return userService.loginSms(interactionData, httpSession);
    }

    @GetMapping("/test")
    public String test() {
        log.info("请求成功");
        return "asdsadasd";
    }
}
