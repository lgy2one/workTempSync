package com.qg.exclusiveplug.util;


import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author WilderGao
 * time 2018-09-23 11:49
 * motto : everything is no in vain
 * description
 */
@Slf4j
public class DateUtil {
    private static String PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static String DAY_PATTERN = "yyyy-MM-dd";

    public static String currentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
        return formatter.format(LocalDateTime.now());
    }

    public static Date getCurrentDate()  {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN);
        try {
            return simpleDateFormat.parse(currentTime());
        } catch (ParseException e) {
            log.warn(currentTime() + "转换格式失败");
            e.printStackTrace();
        }
        //TODO 待修正
        return null;
    }


    /**
     * 得到两个时间相差的秒数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 相差的秒数
     */
    public static int diffSecond(Date startTime, Date endTime) {
        return (int) ((endTime.getTime() - startTime.getTime()) / 1000);
    }


    /**
     * 获得该日期前几天的数据
     *
     * @param date 当前日期
     * @param num  前n天的数据
     * @return 前n天日期数组
     */
    public static String[] getDayBefore(Date date, int num) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DAY_PATTERN);
        String[] days = new String[num];
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);

        for (int i = 1; i <= num; i++) {
            LocalDateTime dateTime = localDateTime.minusDays(i);
            days[i - 1] = formatter.format(dateTime);
        }
        return days;
    }

    /**
     * String 转 date
     *
     * @param dateString 字符串表达式
     * @param pattern    转化格式
     * @return 返回结果
     */
    public static Date stringToDate(String dateString, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            log.info("日期转换格式有误");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断是否符合yyyy-MM-dd HH:mm:ss
     * @param patternString 时间
     * @return 结果
     */
    public static boolean isTimeLegal(String patternString) {

        Pattern a = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}");
        Matcher b = a.matcher(patternString);
        return b.matches();
    }

}
