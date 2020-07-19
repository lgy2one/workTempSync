package com.qg.exclusiveplug.service.impl;

import com.aliyuncs.exceptions.ClientException;
import com.qg.exclusiveplug.constant.DMUrlEnum;
import com.qg.exclusiveplug.constant.DeviceStatusEnum;
import com.qg.exclusiveplug.constant.SmsEnum;
import com.qg.exclusiveplug.constant.StatusEnum;
import com.qg.exclusiveplug.dao.QueryDeviceMapper;
import com.qg.exclusiveplug.dtos.Data;
import com.qg.exclusiveplug.dtos.InteractBigData;
import com.qg.exclusiveplug.dtos.RequestData;
import com.qg.exclusiveplug.dtos.ResponseData;
import com.qg.exclusiveplug.handlers.MyWebSocketHandler;
import com.qg.exclusiveplug.map.*;
import com.qg.exclusiveplug.model.Device;
import com.qg.exclusiveplug.model.DevicePowerRange;
import com.qg.exclusiveplug.service.TcpService;
import com.qg.exclusiveplug.util.DateUtil;
import com.qg.exclusiveplug.util.HttpClientUtil;
import com.qg.exclusiveplug.util.SmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * @author WilderGao
 * time 2018-09-23 10:37
 * motto : everything is no in vain
 * description
 */
@Service
@Slf4j
public class TcpServiceImpl implements TcpService {

    @Resource
    private QueryDeviceMapper queryDeviceMapper;

    private List<DevicePowerRange> powerRangeList;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_KEY = "devices";

    private static final String UNKNOW = "UNKNOW";

    // 待机时间
    private static final long standByTime = 5 * 60 * 60 ;

    private static Map<Integer, Double> maxPower = new HashMap<>();

    private static Map<Integer, Double> minPower = new HashMap<>();

    private static Map<Integer, Double> minPf = new HashMap<>();

    private static Map<Integer, Double> maxPf = new HashMap<>();

    private static Map<Integer, CysQueue> powerQueueMap = new HashMap<>();

    private static Map<Integer, CysQueue> pfQueueMap = new HashMap<>();

    private static final int rateRange = 3;

    @Override
    public void messageHandler(String message) {
        analysisMessage(message);
    }

    /**
     * 将tcp收到的消息解析成设备对象
     *
     * @param message 消息
     */
    private void analysisMessage(String message) {
        powerRangeList = queryDeviceMapper.listAllDevicePowerRange();
//        log.info("所有的训练模型：{}", powerRangeList);

        //解析参数
        String[] list = message.split("end");
        for (String s : list) {
            //查看是哪个串口
            int index = (int) s.charAt(s.length() - 1) - 48;
            s = s.substring(0, s.length() - 1);
            //得到单个插口所有参数信息
            String[] plugs = s.split(",");
//            String name = plugs[0].split(":")[0];
            double voltage = Double.parseDouble(plugs[0].split(":")[2]);
            double current = Double.parseDouble(plugs[1].split(":")[1]);
            double power = Double.parseDouble(plugs[2].split(":")[1]);
            double powerFactor = Double.parseDouble(plugs[3].split(":")[1]);
            double frequency = Double.parseDouble(plugs[4].split(":")[1]);
            double cumulativePower = Double.parseDouble(plugs[5].split(":")[1]);
            int stop = Integer.parseInt(plugs[6].split(":")[1]);
            String currentTime = DateUtil.currentTime();

            Device device = new Device(index, null, current, voltage, power, powerFactor, frequency, currentTime, cumulativePower, stop);

//            log.info("处理前设备信息：{}", device);
            int status = DeviceStatusEnum.UNKNOW.getIndex();
            String prepareName = UNKNOW + index;
            if (device.getPower() < 0.000001) {
                // 功率过小，设置为断电
                status = DeviceStatusEnum.OUTAGE.getIndex();
                for (DevicePowerRange powerRange : powerRangeList) {
                    if (powerRange.getIndex() == index) {
                        prepareName = powerRange.getName();
                    }
                }
                log.info("设备断电：{}", device);
            } else {
                // 设备非断电，根据功率因素筛选范围
                if (null == pfQueueMap.get(index)) {
                    pfQueueMap.put(index, new CysQueue(rateRange));
                }

                CysQueue pfQueue = pfQueueMap.get(index);
                Double frontValue = pfQueue.enQueue(powerFactor);

                Double minPfValue = minPf.get(index);
                Double maxPfValue = maxPf.get(index);

                if (null == frontValue) {
                    // 更新最小功率因素
                    if (null == minPfValue || minPfValue > powerFactor) {
                        minPf.put(index, powerFactor);
                    }
                    // 更新最大功率因素
                    if (null == maxPfValue || maxPfValue < powerFactor) {
                        maxPf.put(index, powerFactor);
                    }
                } else {
                    // 若有弹出值，则能保证相应的最大值最小值不为null
                    // 弹出的是最大值
                    if (Math.abs(frontValue - maxPfValue) < 0.00001) {
                        maxPf.put(index, pfQueue.getMaxValue());
                    }
                    if (Math.abs(frontValue - minPfValue) < 0.00001) {
                        minPf.put(index, pfQueue.getMinValue());
                    }
                }

                // 更新数值
                Double avgPfValue = pfQueue.getAvgValue();
                minPfValue = minPf.get(index);
                maxPfValue = maxPf.get(index);

                log.info("设备：{}， 最小功率因素：{}， 最大功率因素{}， 平均功率因素{}， 本次功率因素：{}", index, minPfValue, maxPfValue, avgPfValue, power);

                List<DevicePowerRange> removeList = new ArrayList<>();
                for (DevicePowerRange powerRange : powerRangeList) {
                    Double maxPf = powerRange.getMaxPf();
                    Double minPf = powerRange.getMinPf();
                    if (minPf - 0 < 0.00001 || maxPf - 0 < 0.00001) {
                        continue;
                    }

                    if (minPfValue < minPf || maxPfValue > maxPf) {
                        removeList.add(powerRange);
                        continue;
                    }

                    if (avgPfValue < minPf || avgPfValue > maxPf) {
                        removeList.add(powerRange);
                    }
                }

                powerRangeList.removeAll(removeList);

                // 结束功率因素的判断
                // 使用功率得出第一个结果
                if (null == powerQueueMap.get(index)) {
                    powerQueueMap.put(index, new CysQueue(rateRange));
                }

                CysQueue powerQueue = powerQueueMap.get(index);
                frontValue = powerQueue.enQueue(power);

                Double minPowerValue = minPower.get(index);
                Double maxPowerValue = maxPower.get(index);

                if (null == frontValue) {
                    // 更新最小功率
                    if (null == minPowerValue || minPowerValue > power) {
                        minPower.put(index, power);
                    }

                    // 更新最大功率因素
                    if (null == maxPowerValue || maxPowerValue < power) {
                        maxPower.put(index, power);
                    }
                } else {
                    // 弹出的值可能既是最大值也是最小值
                    // 弹出的是最大值
                    if (Math.abs(frontValue - maxPowerValue) < 0.00001) {
                        maxPower.put(index, powerQueue.getMaxValue());
                    }
                    if (Math.abs(frontValue - minPowerValue) < 0.00001) {
                        minPower.put(index, powerQueue.getMinValue());
                    }
                }

                // 更新数值
                Double avgPowerValue = powerQueue.getAvgValue();
                minPowerValue = minPower.get(index);
                maxPowerValue = maxPower.get(index);

                log.info("设备：{}， 最小功率：{}， 最大功率{}， 平均功率{}， 本次功率：{}", index, minPowerValue, maxPowerValue, avgPowerValue, power);

                for (DevicePowerRange powerRange : powerRangeList) {
                    double maxPower = powerRange.getMaxPower();
                    double minPower = powerRange.getMinPower();
                    if ((minPowerValue > minPower && maxPowerValue < maxPower) && (avgPowerValue > minPower && avgPowerValue < maxPower)) {
                        device.setName(powerRange.getName());
                        status = powerRange.getStatus();
                        log.info("根据数据库模型判断device：{} 的状态为{}", powerRange.getName(), status);
                        break;
                    }
                    if (powerRange.getIndex() == device.getIndex()) {
                        log.info("按照端口设置名称：{}", device);
                        prepareName = powerRange.getName();
                    }
                }
            }

            device.setName(Optional.ofNullable(device.getName()).orElse(prepareName));

            // 若名字已训练树蛙模型则可进行访问。
            /*if (!device.getName().equals(UNKNOW + index) && status != DeviceStatusEnum.OUTAGE.getIndex()
                    && status != DeviceStatusEnum.UNKNOW.getIndex()) {
                try {
                    status = sendDeviceToDM(device);
                } catch (final Exception ignored) {
                }
            }*/

//            log.info("处理后设备信息：{}", device);

            DeviceStatusHolder.put(device.getIndex(), status);
            // 更新待机信息
            standBy(device, status);
            device.setStatus(status);
            redisTemplate.opsForList().leftPush(CACHE_KEY, device);

            // 如果需要发送数据
            if (WebSocketHolder.containsKey(index)) {
                // 将数据传回给前端
                send(device, status);
            }
        }
    }

    /**
     * 向数据挖掘端发送设备信息并得到设备状态
     *
     * @param device 设备信息
     */
    private int sendDeviceToDM(Device device) {
        ResponseData responseData = new ResponseData();

        // 将设备信息放入交互类
        RequestData<Device> requestData = new RequestData<>();
        requestData.setData(device);
        InteractBigData interactBigData = null;

        // 与数据挖掘端交互
        try {
            interactBigData = HttpClientUtil.demandedCount(DMUrlEnum.JUDGE_STATUS.getDMUrl(), requestData);
        } catch (IOException e) {
            log.debug("数据挖掘端连接失败");
            responseData.setStatus(StatusEnum.PREDICTED_FAILED.getStatus());
            e.printStackTrace();
        }

        if (null != interactBigData) {
            return interactBigData.getStatus();
        }

        // 返回信息
        return 0;
    }

    private void send(Device device, int status) {
        // 设置交互数据
        Data data = new Data();
        data.setDevice(device);
        data.setStatus(status);
        data.setLongAwaitList(LongWaitList.getLongWaitList());
        data.setDeviceStatus(DeviceStatusHolder.getMAP());
        ResponseData responseData = new ResponseData();
        responseData.setStatus(StatusEnum.NORMAL.getStatus());
        responseData.setData(data);
        MyWebSocketHandler.send(device.getIndex(), responseData);
    }

    /**
     * 待机队列
     */
    private void standBy(Device device, int status) {
        int index = device.getIndex();
        // 判断长时间待机状态
        if (status == DeviceStatusEnum.STANDBY.getIndex()) {
            // 更新timeMap时间
            if (!TimeMap.containsKey(index)) {
                TimeMap.put(index, DateUtil.getCurrentDate());
            } else {
                int diffSecond = DateUtil.diffSecond(TimeMap.get(index), DateUtil.getCurrentDate());
                log.info(device.getName() + "已挂机" + diffSecond + "秒");
                // 判断待机持续时间
                if (diffSecond > standByTime) {
                    // 加入长时间待机警示队列并发送短信
                    if (!LongWaitList.contains(index)) {
                        try {
                            log.info("发送短信");
                            SmsUtil.sendSms("18929824809",
                                    "{\"name\":\" " + device.getName() + " \"}",
                                    SmsEnum.DEVICE_STANDBY_LONGTIME.getTemplateCode());
                        } catch (ClientException e) {
                            log.error("短信发送失败");
                            e.printStackTrace();
                        }
                        LongWaitList.add(index);
                    }
                } else {
                    // 移出长时间待机警示队列
                    if (LongWaitList.contains(index)) {
                        LongWaitList.remove(index);
                    }
                }
            }
        } else {
            // 更新最近检测时间和待机队列
            TimeMap.replace(index, DateUtil.getCurrentDate());
            if (LongWaitList.contains(index)) {
                LongWaitList.remove(index);
            }
        }
    }
}
