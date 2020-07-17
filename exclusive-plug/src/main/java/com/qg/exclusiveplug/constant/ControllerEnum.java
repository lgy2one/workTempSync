package com.qg.exclusiveplug.constant;

public enum ControllerEnum {
    /**
     * 关闭用电器
     */
    CLOSE(0),

    /**
     * 启动用电器
     */
    OPEN(1);


    private int status;

    ControllerEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
