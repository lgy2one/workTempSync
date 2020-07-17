package com.qg.exclusiveplug.service;

/**
 * @author WilderGao
 * time 2018-09-23 17:59
 * motto : everything is no in vain
 * description
 */
public interface TimeService {
    /**
     * 把缓存在redis中的数据保存到MySQL
     */
    void saveDataToMySql() throws InterruptedException;

    /**
     * 收集昨日的数据并统计加入数据库
     */
    void saveStatusToMySql();

    void updateModel();
}
