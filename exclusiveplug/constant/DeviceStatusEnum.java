package com.qg.exclusiveplug.constant;

public enum DeviceStatusEnum {

    /**
     * 断电
     */
    OUTAGE(0),

    /**
     * 待机
     */
    STANDBY(1),

    /**
     * 正常
     */
    NORMAL(2),

    /**
     * 故障
     */
    BROKEN(3),

    /**
     * 未知
     */
    UNKNOW(4);

    private int index;

    DeviceStatusEnum(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
