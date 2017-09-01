package com.zk.demo.utils;

/**
 * Created by Koori_Cc on 2017/8/28.
 */

import com.zk.demo.entities.Device;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TelnetUtils {

    private static final TelnetClient telnet = new TelnetClient();

    private static Logger logger = Logger.getLogger("TelnetUtils");

    public static boolean telnet(String ip,int port) {
        try{
            telnet.connect(ip, port);
            return true;
        }catch(IOException e){
            e.printStackTrace();
            logger.info("telnet连接失败");
            return false;
        }
    }

    public static List<Device> checkDeviceConnection(List<Device> list) {

        //复制list集合
        List<Device> new_list = new ArrayList<Device>();
        for(int i = 0;i < list.size();i++) {
            Device d = new Device();
            d.setId(list.get(i).getId());
            d.setStatus(list.get(i).getStatus());
            d.setIp(list.get(i).getIp());
            d.setPort(list.get(i).getPort());
            new_list.add(d);
        }

        for(int i =0; i < new_list.size();i++) {
            Device d = new_list.get(i);
            String ip = d.getIp();
            String s_port = d.getPort();
            Integer port = Integer.parseInt(s_port);
            boolean result = telnet(ip,port);
            if(result) {
                d.setStatus(1);   //连接畅通
            }else {
                d.setStatus(0);   //连接阻断
            }
        }
        return new_list;
    }


}
