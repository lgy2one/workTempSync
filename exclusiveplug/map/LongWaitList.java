package com.qg.exclusiveplug.map;

import java.util.LinkedList;
import java.util.List;

/**
 * 记录长时间待机队列
 */
public class LongWaitList {
    private static final List<Integer> longWaitList = new LinkedList<>();

    public static List<Integer> getLongWaitList() {
        return longWaitList;
    }

    public static void add(Integer index) {
        longWaitList.add(index);
    }

    public static boolean contains(Integer index) {
        return longWaitList.contains(index);
    }

    public static void remove(Integer index) {
        longWaitList.remove(index);
    }
}
