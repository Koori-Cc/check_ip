package com.zk.demo.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zk.demo.entities.Table;
import com.zk.demo.services.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

import java.util.logging.Logger;

/**
 * Created by Koori_Cc on 2017/8/30.
 */
@Controller
@RequestMapping("/device")
public class DeviceController {

    private Logger logger = Logger.getLogger("DeviceController");



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
    public Object autoUpdateData(Integer time,Table table,Integer auto_flag) {
        deviceService.startAutoUpdate(time,table);
        if(auto_flag == 0) {   //说明之前未进行自动更新
            auto_flag = 1;
        }
        return auto_flag;
    }

    @RequestMapping("/cancleTimer.do")
    @ResponseBody
    public Object cancleTimer(Integer auto_flag) {
        deviceService.closeAutoUpdate();
        if(auto_flag == 1) {
            auto_flag = 0;
        }
        return auto_flag;
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
