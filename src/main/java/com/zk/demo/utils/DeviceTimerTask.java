package com.zk.demo.utils;

import com.zk.demo.entities.Table;
import com.zk.demo.services.DeviceService;

import java.util.TimerTask;

/**
 * Created by Koori_Cc on 2017/9/4.
 */
public class DeviceTimerTask extends TimerTask {


    private DeviceService deviceService;
    private Table table;

    public DeviceTimerTask() {}

    public DeviceTimerTask(DeviceService deviceService,Table table) {
        this.deviceService=deviceService;
        this.table=table;
    }

    public void run() {
        deviceService.updateStatus(table);
    }

}

