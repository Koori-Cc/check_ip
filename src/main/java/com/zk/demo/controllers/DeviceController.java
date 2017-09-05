package com.zk.demo.controllers;

import com.zk.demo.services.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


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
    public Object getData( @RequestParam String[] ids) {
        logger.info(ids.toString());
        return deviceService.getData(ids);
    }

    @RequestMapping("/updateData.do")
    @ResponseBody
    public Object updateData(@RequestParam String[] ids) {
        Integer result = deviceService.updateStatus(ids);
        return result;
    }

    @RequestMapping("/autoUpdateData.do")
    @ResponseBody
    public Object autoUpdateData(Integer time,String id,Integer auto_flag) {
        String[] ids = id.split(",");
        deviceService.startAutoUpdate(time,ids);
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

}
