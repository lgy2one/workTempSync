package com.qg.exclusiveplug.controller;

import com.qg.exclusiveplug.dtos.InteractionData;
import com.qg.exclusiveplug.dtos.ResponseData;
import com.qg.exclusiveplug.service.QueryDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.ParseException;


/**
 * @author Wilder
 */
@RestController
@Slf4j
@CrossOrigin
@RequestMapping("/querydevice")
public class QueryDeviceController {
    @Resource
    private QueryDeviceService queryDeviceService;

    @RequestMapping("/pastpowersum")
    public ResponseData getPassPowerSum(@RequestBody InteractionData interactionData) {
        ResponseData responseData = null;
        try {
            responseData = queryDeviceService.listPowerSum(interactionData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return responseData;
    }

    @PostMapping("/queryindex")
    public ResponseData queryIndexs(HttpSession httpSession) {
        return queryDeviceService.queryIndexs(httpSession);
    }

}
