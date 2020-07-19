package com.qg.exclusiveplug.constant;

public enum  UserEnum {
    /**
     * 关闭用电器
     */
    LOGIN("LOGIN"),

    /**
     * 启动用电器
     */
    REGISTER("REGISTER");


    private String status;

    UserEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
