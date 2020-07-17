package com.qg.exclusiveplug.dao;

import com.qg.exclusiveplug.model.Device;
import com.qg.exclusiveplug.model.DevicePowerRange;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author WilderGao
 * time 2018-09-23 16:57
 * motto : everything is no in vain
 * description 与电器有关的数据库操作
 */
@Mapper
public interface QueryDeviceMapper {

    /**
     * 保存设备到数据库
     *
     * @param devices 设备集合
     * @return 插入结果
     */
    int saveDevices(@Param("devices") List<Device> devices,@Param("tableName")String tableName) throws Exception;

    /**
     * 取得某串口某时间段内的总用电量
     *
     * @param index     串口号
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 某串口某时间段内的总用电量
     */
    Double listPowerSum(@Param("index") int index, @Param("startTime") String startTime, @Param("endTime") String endTime,@Param("tableName")String tableName);

    List<Device> listAllDevice(@Param("index") int index, @Param("tabeleName") String tableName);

    List<DevicePowerRange> listAllDevicePowerRange();

    List<DevicePowerRange> listAboutDevicePowerRange(@Param("name") String deviceName, @Param("index") int DeviceIndex);

    List<DevicePowerRange> listDevicePowerWithOutIndexZero();

    List<Double> listPowerByDeviceNameWithOutPowerZero(@Param("deviceName") String deviceName, @Param("table") String table);

    DevicePowerRange getDevicePowerRangeByIndex(@Param("index") int index);
}
