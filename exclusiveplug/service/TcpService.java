package com.qg.exclusiveplug.service;

/**
 * @author WilderGao
 * time 2018-09-23 10:37
 * motto : everything is no in vain
 * description tcp服务类
 */
public interface TcpService {
    /**
     * 对tcp传送的数据进行处理
     *
     * @param message 传送的数据
     */
    void messageHandler(String message);
}
