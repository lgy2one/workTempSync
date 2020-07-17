package com.qg.exclusiveplug.map;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 记录某串口最后待机的时间
 */
public class TimeMap {
    private static final Map<Integer, Date> map = new ConcurrentHashMap<>(16);

    public static void put(Integer index, Date date) {
        map.put(index, date);
    }

    public static Date get(Integer index) {
        return map.get(index);
    }

    public static Map<Integer, Date> getMAP() {
        return map;
    }

    public static boolean containsKey(Integer index) {
        return map.containsKey(index);
    }

    public static void replace(Integer index, Date date) {
        map.replace(index, date);
    }

    public static void remove(Integer index) {
        map.remove(index);
    }
}
