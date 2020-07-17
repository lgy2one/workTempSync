package com.qg.exclusiveplug.map;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author HC
 * @create 2019-05-24 15:37
 */
public class DeviceStatusHolder {
    private static final Map<Integer, Integer> map = new ConcurrentHashMap<>(16);

    public static void put(Integer deviceIndex, Integer status) {
        map.put(deviceIndex, status);
    }

    public static Map<Integer, Integer> getMAP() {
        return map;
    }

    public static Integer get(Integer deviceIndex) {
        return map.get(deviceIndex);
    }

    public static void removeKey(Integer deviceIndex) {
        map.remove(deviceIndex);
    }
}
