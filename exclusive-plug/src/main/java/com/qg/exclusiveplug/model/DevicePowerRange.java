package com.qg.exclusiveplug.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DevicePowerRange {
    private String name;

    private double minPower;

    private double maxPower;

    private double center;

    private double minPf;

    private double maxPf;

    private int status;

    private int index;

    public DevicePowerRange(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public DevicePowerRange(String name, double minPower, double maxPower, int status) {
        this.name = name;
        this.minPower = minPower;
        this.maxPower = maxPower;
        this.status = status;
    }
}
