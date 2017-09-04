package com.zk.demo.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zk.demo.daos.DeviceDao;
import com.zk.demo.entities.Device;
import com.zk.demo.entities.Table;
import com.zk.demo.services.DeviceService;
import com.zk.demo.utils.DeviceTimerTask;
import com.zk.demo.utils.LoggerUtils;
import com.zk.demo.utils.TelnetUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by Koori_Cc on 2017/8/30.
 */
@Service
public class DeviceServiceImpl implements DeviceService{

    @Autowired
    private DeviceDao deviceDao;

    private Logger logger = Logger.getLogger("DeviceServiceImpl");

    private static ResourceBundle bundle = ResourceBundle.getBundle("config");

    //定义Jedis成员变量
    private static Jedis jedis;

    //redis存储的key
    private static String key;

    private static String totalTableName;

    //定时器
    private Timer timer;

    /**
     * 类加载的时候初始化redis参数
     */
    static {
        String redis_ip = bundle.getString("redis.url");
        String s_port = bundle.getString("redis.port");
        Integer redis_port = Integer.parseInt(s_port);
        //jedis初始化完成
        jedis = new Jedis(redis_ip,redis_port);
        //获取所有表的名称
        totalTableName = bundle.getString("table.total.name");
    }

    //初始化的方法
    private static void init(Table table) {
        //用表名当作键来区分不同表的数据
        key = bundle.getString(table.getTableName());

        table.setField_id(bundle.getString(table.getTableName() + ".id"));
        table.setField_ip(bundle.getString(table.getTableName() + ".ip"));
        table.setField_port(bundle.getString(table.getTableName() + ".port"));
        table.setField_status(bundle.getString(table.getTableName() + ".status"));

    }

    public boolean getData(Table table) {

        //初始化table
        init(table);

        boolean result;
        try {
            List<Device> list = deviceDao.queryAllData(table);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(list);
            jedis.set(key,json);
            if(list.size() == 0) {
                result = false;
            }else {
                result = true;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.info("获取数据失败!");
            result = false;
        }
        return result;
    }

    public Integer updateStatus(Table table) {

        init(table);

        String json = jedis.get(key);
        if(json == null) {   //未进行数据的初始化
            return -1;
        }
        Integer result = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            //数据库查询得到的list
            List<Device> db_list = mapper.readValue(json, new TypeReference<List<Device>>() {});
            //检测数据状态,并返回一个改变状态之后的集合
            List<Device> temp_list = TelnetUtils.checkDeviceConnection(db_list);
            //真正执行更新操作的list集合
            List<Object> update_list = new ArrayList<Object>();
            for (int i = 0;i < db_list.size();i++) {

                if(db_list.get(i).getStatus() != temp_list.get(i).getStatus()) {   //说明状态值发生改变
                    Device d = new Device();
                    d.setId(temp_list.get(i).getId());
                    d.setStatus(temp_list.get(i).getStatus());
                    update_list.add(d);

                    //打印日志
                    String msg;
                    if(temp_list.get(i).getStatus() == 0) {
                        msg = temp_list.get(i).getId() +"---" + temp_list.get(i).getIp()+" : "+temp_list.get(i).getPort() + "  转为阻断状态";
                    }else {
                        msg = temp_list.get(i).getId() +"---" + temp_list.get(i).getIp()+" : "+temp_list.get(i).getPort() + "  转为畅通状态";
                    }
                    LoggerUtils.log(msg);
                }
            }
            //说明有数据
            if(update_list.size()!=0) {
                Map<Object,Object> map = new HashedMap();
                map.put("objectList",update_list);
                map.put("table",table);
                result = deviceDao.updateStatus(map);
            }else {
                result = 0;
            }
            //更新完成后同步redis数据
            List<Device> list = deviceDao.queryAllData(table);
            String new_json = mapper.writeValueAsString(list);
            jedis.set(key,new_json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<String> getTableName() {
        List<String> list = new ArrayList<String>();
        String[] arr = totalTableName.split(",");
        for (String name: arr ) {
            list.add(name);
        }
        return list;
    }

    public void startAutoUpdate(Integer time, Table table) {
        Timer timer = new Timer();   //定时器销毁就不能用了,要启用一个新的定时器
        this.timer = timer;
        init(table);
        Integer msec = time * 60 * 1000;
        timer.schedule(new DeviceTimerTask(this,table) , new Date() , msec);
    }

    public void closeAutoUpdate() {
        timer.cancel();
    }
}
