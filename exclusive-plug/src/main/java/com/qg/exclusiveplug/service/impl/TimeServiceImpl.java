package com.qg.exclusiveplug.service.impl;

import com.qg.exclusiveplug.constant.DMUrlEnum;
import com.qg.exclusiveplug.dao.ActionDeviceMapper;
import com.qg.exclusiveplug.dao.CreateTableMapper;
import com.qg.exclusiveplug.dao.Provider;
import com.qg.exclusiveplug.dao.QueryDeviceMapper;
import com.qg.exclusiveplug.dtos.InteractBigData;
import com.qg.exclusiveplug.dtos.RequestData;
import com.qg.exclusiveplug.model.Device;
import com.qg.exclusiveplug.model.DevicePowerRange;
import com.qg.exclusiveplug.service.TimeService;
import com.qg.exclusiveplug.util.DateUtil;
import com.qg.exclusiveplug.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author WilderGao
 * time 2018-09-23 18:03
 * motto : everything is no in vain
 * description
 */
@Service
@Slf4j
public class TimeServiceImpl implements TimeService {
    private static final String CACHE_KEY = "devices";

    @Resource
    private RedisTemplate<String, Device> redisTemplate;

    @Resource
    private QueryDeviceMapper queryDeviceMapper;

    @Resource
    private ActionDeviceMapper actionDeviceMapper;

    @Resource
    private CreateTableMapper createTableMapper;

    @Override
    @Async
    @Scheduled(fixedRate = 20000)
    public void saveDataToMySql() {
        if (null != redisTemplate.hasKey(CACHE_KEY) && redisTemplate.hasKey(CACHE_KEY)) {
            // 开启事务
            redisTemplate.multi();
            redisTemplate.opsForList().range(CACHE_KEY, 0, -1);
            redisTemplate.opsForList().trim(CACHE_KEY, 1, 0);
            List<Object> objects = redisTemplate.exec();
            // 事物没有回滚
            if (objects.size() != 0) {
                @SuppressWarnings("unchecked")
                List<Device> devices = (List<Device>) objects.get(0);

                log.info("定时存储-->>" + devices);
                assert devices != null;
                Date date;
                date = DateUtil.getCurrentDate();
                String tableName = "device" + new SimpleDateFormat("yyyyMMdd").format(date);
                System.out.println(tableName);
                try {
                    queryDeviceMapper.saveDevices(devices, tableName);
                } catch (Exception e) {
                    //if table is not exists.will catch a ex.and create a table and save data;
                    try {
                        createTableMapper.updateTableField(Provider.createTableSql(tableName));
                        queryDeviceMapper.saveDevices(devices, tableName);
                    } catch (Exception e1) {
                        log.error("自动建表并且存储数据失败");
                    }
                }
                System.out.println(devices.size());
            }
        }
    }

    /**
     * 收集昨日的数据并统计加入数据库
     */
    @Override
    @Async
    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    public void saveStatusToMySql() {
        Date currentDate = new Date();
        currentDate = DateUtil.getCurrentDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

    }

        /*if (CacheMap.containKey(CACHE_KEY)) {
            List<Device> devices = CacheMap.get(CACHE_KEY);
            log.info("将map中的数据保存到mysql并删除");
            //首先获取数据，并且尝试插入；
            assert devices != null;
            Device device = devices.get(0);
            //2018-11-10 22:30:29
            String date = device.getDate().substring(0,10);
            date=date.replaceAll("-","");
            String tableName="device"+date;
            try {
                queryDeviceMapper.saveDevices(devices,tableName );
            } catch (Exception e) {
                //if table is not exists.will catch a ex.and create a table and save data;
                try {
                    createTableMapper.updateTableField(Provider.createTableSql(tableName));
                    queryDeviceMapper.saveDevices(devices,tableName );
                } catch (Exception e1) {
                    log.error("自动建表并且存储数据失败");
                }
            }
            CacheMap.remove(CACHE_KEY);
        }*/

    @Override
    @Async
    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void updateModel() {
        // 准备训练模型的功率范围
        List<DevicePowerRange> prepareDevicePowerRange = queryDeviceMapper.listDevicePowerWithOutIndexZero();

        if (null != prepareDevicePowerRange) {
            prepareDevicePowerRange.forEach(devicePowerRange -> {
                DateUtil.getCurrentDate();
                Date date = DateUtil.getCurrentDate();
                String tableName = "device" + new SimpleDateFormat("yyyyMMdd").format(date);
//                String tableName = "device20190814";
                log.info("训练模型：{}， 模型训练时间：{}， 查询表名：{}", devicePowerRange.getName(), date, tableName);

                List<Double> doubleList = queryDeviceMapper.listPowerByDeviceNameWithOutPowerZero(devicePowerRange.getName(), tableName);

                // 若数据量不足则查询上一天数据
                if (doubleList.size() < 10000) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.DAY_OF_YEAR, -1);
                    doubleList.addAll(queryDeviceMapper.listPowerByDeviceNameWithOutPowerZero(devicePowerRange.getName(), tableName));
                }

                // 封装数据
                RequestData requestData = new RequestData<>();
                requestData.setPowerSums(doubleList);
                requestData.setName(devicePowerRange.getName());

                // 提交数据给DM训练模型
                InteractBigData interactBigData = connectToDMFroTrainModel(requestData);
                if (null != interactBigData && 0 != interactBigData.getStatus()) {
                    log.info("模型{}训练成功", devicePowerRange.getName());
                    List<DevicePowerRange> devicePowerRangeList = interactBigData.getDevicePowerRange();
                    List<DevicePowerRange> resultDevicePowerRangeList = new ArrayList<>();
                    devicePowerRangeList.forEach(devicePowerRange1 -> {
                        double center = devicePowerRange1.getCenter();
                        if (center > 100) {
                            resultDevicePowerRangeList.add(new DevicePowerRange(devicePowerRange1.getName(), center - 10, center + 10, devicePowerRange1.getStatus()));
                        } else if (center > 10) {
                            resultDevicePowerRangeList.add(new DevicePowerRange(devicePowerRange1.getName(), center - 1, center + 1, devicePowerRange1.getStatus()));
                        } else if (center > 0.5) {
                            resultDevicePowerRangeList.add(new DevicePowerRange(devicePowerRange1.getName(), center - 0.05, center + 0.05, devicePowerRange1.getStatus()));
                        } else {
                            resultDevicePowerRangeList.add(new DevicePowerRange(devicePowerRange1.getName(), center - 0.005, center + 0.005, devicePowerRange1.getStatus()));
                        }
                    });
                    if (!resultDevicePowerRangeList.isEmpty()) {
                        log.info("训练模型成功");
                    }

//                    actionDeviceMapper.updateDevicePowerRange(devicePowerRange);
//                    actionDeviceMapper.delDevicePowerByIndex(devicePowerRange.getIndex());
                    actionDeviceMapper.insertAllDevicePowerRange(resultDevicePowerRangeList);
                }
            });
        }
    }

    private InteractBigData connectToDMFroTrainModel(RequestData requestData) {
        InteractBigData interactBigData = null;

        // 与数据挖掘端交互
        try {
            interactBigData = HttpClientUtil.demandedCount(DMUrlEnum.TRAIN_ONE.getDMUrl(), requestData);
        } catch (IOException e) {
            log.debug("数据挖掘端连接失败");
            e.printStackTrace();
        }
        return interactBigData;
    }
}
