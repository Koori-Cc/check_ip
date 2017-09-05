package com.zk.demo.utils;

import com.zk.demo.entities.Table;
import com.zk.demo.services.DeviceService;

import java.util.Map;
import java.util.TimerTask;

/**
 * Created by Koori_Cc on 2017/9/4.
 */
public class DeviceTimerTask extends TimerTask {


    private DeviceService deviceService;
    private String[] ids;

    public DeviceTimerTask(DeviceService deviceService,String[] ids) {
        this.deviceService=deviceService;
        this.ids=ids;
    }

    public void run() {
        deviceService.updateStatus(ids);
    }

}

