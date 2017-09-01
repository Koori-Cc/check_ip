package com.zk.demo.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zk.demo.entities.Table;
import com.zk.demo.services.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
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
    public Object getData(Table table) {
        return deviceService.getData(table);
    }

    @RequestMapping("/updateData.do")
    @ResponseBody
    public Object updateData(Table table) {
        Integer result = deviceService.updateStatus(table);
        return result;
    }

    @RequestMapping("/autoUpdateData.do")
    @ResponseBody
    public void autoUpdateData(Integer time,final Table table) {
        Integer msec = time * 60 * 1000;
        timer.schedule(new TimerTask() {
            public void run() {
                deviceService.updateStatus(table);
            }
        } , new Date() , msec);
    }

    @RequestMapping("/cancleTimer.do")
    @ResponseBody
    public void cancleTimer() {
        timer.cancel(); // 定时器销毁
    }

    @RequestMapping("/getTableName.do")
    @ResponseBody
    public String getTableName() {
        ObjectMapper mapper = new ObjectMapper();
        List<String> list = deviceService.getTableName();
        String json = null;
        try {
            json = mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }


}
