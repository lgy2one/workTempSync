/*
package com.qg.exclusiveplug.listener;

import com.qg.exclusiveplug.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Configuration
@Order(1)
@Profile(value = "dev")
public class BeforeStartup implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        // 设置定时时间
        final long timeInterval = 24 * 60 * 60 * 1000;
        List<String> machineNameList = new ArrayList<>();
        // 设置设备列表
        machineNameList.add(SerialPortEnum.INDEX_1.getSerialPort());
        machineNameList.add(SerialPortEnum.INDEX_2.getSerialPort());
        machineNameList.add(SerialPortEnum.INDEX_3.getSerialPort());

        // 启动定时线程任务
        new Thread(() -> {
            while (true) {
                String date = DateUtil.currentTime().split(" ")[0];
                // 提前一天
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(sdf.parse(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                for (String machineName : machineNameList) {
                    dataService.listDevicesCPByDate(machineName, sdf.format(calendar.getTime()));
                }
                try {
                    Thread.sleep(timeInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
*/
