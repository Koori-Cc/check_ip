package com.zk.demo.controllers;


import com.zk.demo.services.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * Created by Koori_Cc on 2017/8/30.
 */
@Controller
@RequestMapping("/device")
public class DeviceController {

    private Logger logger = Logger.getLogger("DeviceController");

    private final Timer timer = new Timer();

    @Autowired
    private DeviceService deviceService;

    @RequestMapping("/toMenu.do")
    public String toMenu() {
        return "menu";
    }


    @RequestMapping("/getData.do")
    @ResponseBody
    public Object getData() {
        return deviceService.getData();
    }

    @RequestMapping("/updateData.do")
    @ResponseBody
    public Object updateData() {
        Integer result = deviceService.updateStatus();
        return result;
    }

    @RequestMapping("/autoUpdateData.do")
    @ResponseBody
    public void autoUpdateData(Integer time) {
        Integer msec = time * 60 * 1000;
        timer.schedule(new TimerTask() {
            public void run() {
                deviceService.updateStatus();
            }
        } , new Date() , msec);
    }

    @RequestMapping("/cancleTimer.do")
    @ResponseBody
    public void cancleTimer() {
        timer.cancel(); // 定时器销毁
    }


}
