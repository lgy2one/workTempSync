package com.qg.exclusiveplug.util;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TableUtils {
    private static String PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String produceCurrentTableName() {
        return produceTableName(new Date());
    }

    public static String produceTableName(String nowTime) {
        return "device" + nowTime.split(" ")[0].replaceAll("-", "");
    }

    public static String produceTableName(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
        return produceTableName(sdf.format(date));
    }

    public static void main(String[] args)
    {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        System.out.println(factory.resolveEmbeddedValue("${logging.file}"));
    }
}
