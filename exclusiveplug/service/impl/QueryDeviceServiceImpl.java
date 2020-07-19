package com.qg.exclusiveplug.service.impl;

import com.qg.exclusiveplug.constant.StatusEnum;
import com.qg.exclusiveplug.dao.QueryDeviceMapper;
import com.qg.exclusiveplug.dtos.Data;
import com.qg.exclusiveplug.dtos.InteractionData;
import com.qg.exclusiveplug.dtos.ResponseData;
import com.qg.exclusiveplug.model.PowerSum;
import com.qg.exclusiveplug.model.User;
import com.qg.exclusiveplug.service.QueryDeviceService;
import com.qg.exclusiveplug.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class QueryDeviceServiceImpl implements QueryDeviceService {
    /**
     * 数据库开始时刻
     */
    private final static String START_TIME = "2018-11-30 00:00:00";

    /**
     * 日期格式
     */
    private final static String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 时间格式
     */
    private final static String TIME_PATTERN = "yyyy-MM-dd HH-mm-ss";
    @Autowired
    private QueryDeviceMapper queryDeviceMapper;

    /**
     * 得到某天(24小时)/某月(30天)的用电量
     *
     * @param interactionData key(天/月/周)，时间
     * @return 状态码和用电量集合
     */
    @Override
    public ResponseData listPowerSum(InteractionData interactionData) throws ParseException {
        log.info("得到用电量 -->> 进入");
        if (interactionData.getKey() > 6 || interactionData.getKey() < 3 || !DateUtil.isTimeLegal(interactionData.getTime())) {
            //参数有误
            log.error("得到用电量 -->> 前端传入参数有误");
            return new ResponseData(StatusEnum.PARAMETER_ERROR.getStatus(), null);
        }
        ResponseData responseData = new ResponseData();
        log.info(String.valueOf(interactionData.getKey()));
        List<PowerSum> powerSumList = new ArrayList<>();

        if (interactionData.getKey() == 3) {
            // 按天查询
            powerSumList = listPowerSumByDay(interactionData.getIndex(), interactionData.getTime());
        } else if (interactionData.getKey() == 4) {
            // 按周查询
            powerSumList = listPowerSumByWeek(interactionData.getIndex(), interactionData.getTime());
        } else {
            // 按月查询
            powerSumList = listPowerSumByMonth(interactionData.getIndex(), interactionData.getTime());
        }

        // 返回数据
        responseData.setStatus(StatusEnum.NORMAL.getStatus());
        Data data = new Data();

        data.setPowerSumList(powerSumList);

        responseData.setData(data);
        return responseData;
    }

    /**
     * 得到用户的所有端口以及权限
     *
     * @return 用户的所有端口以及权限
     */
    @Override
    public ResponseData queryIndexs(HttpSession httpSession) {
        ResponseData responseData = new ResponseData();
        User user = (User) httpSession.getAttribute("user");
        Data data = new Data();
        data.setUser(User.builder().indexPrivilegeMap(user.getIndexPrivilegeMap()).build());
        responseData.setData(data);
        responseData.setStatus(StatusEnum.NORMAL.getStatus());
        return responseData;
    }

    /**
     * 以小时分隔，得到该天的用电量
     *
     * @param deviceIndex 串口
     * @param time  日期
     * @return 该天24小时各自的用电量
     */
    private List<PowerSum> listPowerSumByDay(int deviceIndex, String time) throws ParseException {
        log.info("按天查询");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<PowerSum> powerSumList = new ArrayList<>();

        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        for (int i = 0; i < 24; i++) {
            // 得到起始时间和截至时间的总用电量
            String startTime = sdf.format(calendar.getTime());
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            String endTime = sdf.format(calendar.getTime().getTime());

            powerSumList.add(listPowerSum(deviceIndex, startTime, endTime, startTime.split(" ")[1].split(":")[0]));
        }
        return powerSumList;
    }

    /**
     * 以天分隔，得到该周的用电量
     *
     * @param deviceIndex 串口
     * @param time  日期
     * @return 该周7天各自的用电量
     */
    private List<PowerSum> listPowerSumByWeek(int deviceIndex, String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<PowerSum> powerSumList = new ArrayList<>();
        try {
            Date date = sdf.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            for (int i = 0; i < 7; i++) {
                String startTime = sdf.format(calendar.getTime());
                calendar.add(Calendar.DAY_OF_WEEK, 1);
                String endTime = sdf.format(calendar.getTime().getTime());
                powerSumList.add(listPowerSum(deviceIndex, startTime, endTime, startTime.split(" ")[0].split("-")[2]));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return powerSumList;
    }

    /**
     * 以天分隔，得到该月的用电量
     *
     * @param deviceIndex 串口
     * @param time  日期
     * @return 该月30天各自的用电量
     */
    private List<PowerSum> listPowerSumByMonth(int deviceIndex, String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<PowerSum> powerSumList = new ArrayList<>();
        try {
            Date date = sdf.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            for (int i = 0; i < 30; i++) {
                String startTime = sdf.format(calendar.getTime());
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                String endTime = sdf.format(calendar.getTime().getTime());
                powerSumList.add(listPowerSum(deviceIndex, startTime, endTime, startTime.split(" ")[0].split("-")[2]));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return powerSumList;
    }

    /**
     * 检查查询时间是否处于数据库最小时间与当前时间之间
     *
     * @param startTime 起始时间
     * @param endTime   终止时间
     * @return 判断结果
     */
    private boolean checkTime(String startTime, String endTime) throws ParseException {
        // 查询起始日期要大于数据库最低存储日期并且查询终止日期要小于当前日期
        return DateUtil.diffSecond(DateUtil.stringToDate(START_TIME, DATE_PATTERN),
                DateUtil.stringToDate(startTime, DATE_PATTERN)) > 0 && DateUtil.diffSecond(DateUtil.getCurrentDate(),
                DateUtil.stringToDate(endTime, DATE_PATTERN)) < 0;
    }

    /**
     * 得到用电量列表
     * @param deviceIndex 端口号
     * @param startTime 起始时间
     * @param endTime 终止时间
     * @return 用电量列表
     * @throws ParseException 转换失败异常
     */
    private PowerSum listPowerSum(int deviceIndex, String startTime, String endTime, String token) throws ParseException {
        List<PowerSum> powerSumList = new ArrayList<>();
        PowerSum powerSum = new PowerSum();
        if (checkTime(startTime, endTime)) {
            // 以天为单位
            String tableName = "device" + startTime.split(" ")[0].replaceAll("-", "");
            log.info("查询数据表：" + tableName);
            try {
                powerSum = new PowerSum(token,
                        queryDeviceMapper.listPowerSum(deviceIndex, startTime, endTime, tableName));
            } catch (Exception e) {
                log.info("查询过往数据中数据表不存在:{}", tableName);
                powerSum = new PowerSum(token, 0.0);
            }
            log.info("设备号：" + deviceIndex + "开始时间：" + startTime + "结束时间：" + endTime + "查询数据：" + powerSum);
        } else {
            log.info("查询数据不符合格式,开始时间{}, 结束时间{}", startTime, endTime);
            powerSum = new PowerSum(token, 0.0);
        }
        return powerSum;
    }

}
