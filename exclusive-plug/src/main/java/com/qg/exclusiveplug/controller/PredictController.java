package com.qg.exclusiveplug.controller;

import com.qg.exclusiveplug.dtos.InteractionData;
import com.qg.exclusiveplug.dtos.ResponseData;
import com.qg.exclusiveplug.service.PredictService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.function.BiConsumer;

/**
 * @author WilderGao
 * time 2018-10-03 23:55
 * motto : everything is no in vain
 * description 预测接口
 */
@RestController()
@RequestMapping("/predicted")
@CrossOrigin
public class PredictController {
    @Resource
    private PredictService predictService;

    /**
     * 预测今天的用电量
     *
     * @param interactionData 前端请求参数，包括串口号index 和时间time
     * @return 预测之后的结果
     */
    @PostMapping(value = "/nowpowersum")
    public ResponseData predictTodayPowerSum(@RequestBody InteractionData interactionData) {
        return predictService.predictNowPowerSumService(interactionData.getTime(), interactionData.getIndex());
    }
}
