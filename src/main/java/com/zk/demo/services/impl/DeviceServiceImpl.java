package com.zk.demo.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zk.demo.daos.DeviceDao;
import com.zk.demo.daos.TableDao;
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

    @Autowired
    private TableDao tableDao;

    private Logger logger = Logger.getLogger("DeviceServiceImpl");

    private  ResourceBundle bundle = ResourceBundle.getBundle("config");

    //定义Jedis成员变量
    private Jedis jedis;

    //存储Table对象的集合
    private List<Table> tableList;

    //用于存储table的map集合
    private Map<String,Table> tableMap;

    //定时器
    private Timer timer;

    /**
     * 类加载的时候初始化redis参数
     */
    {
        String redis_ip = bundle.getString("redis.url");
        String s_port = bundle.getString("redis.port");
        Integer redis_port = Integer.parseInt(s_port);
        //jedis初始化完成
        jedis = new Jedis(redis_ip,redis_port);
    }

    //初始化的方法
    private void init(String[] ids) {
        tableList = tableDao.queryTableListByIds(ids);

        tableMap = new HashedMap();
        for (Table table:tableList) {
            tableMap.put(table.getTableName(),table);
        }

    }

    public boolean getData(String[] ids){
        //初始化table
        init(ids);
        boolean result = false;
        try {
            for(String key:tableMap.keySet()) {
                Table table = tableMap.get(key);
                List<Device> list = deviceDao.queryAllData(table);
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(list);
                jedis.set(key, json);
            }
            result = true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Integer updateStatus(String[] ids) {
        init(ids);   //初始化tableMap
        Integer result = 0;
        try {
            //先检测数据是否完整
            for(String key:tableMap.keySet()) {
                Table table = tableMap.get(key);
                String json = jedis.get(key);
                if(json == null) {   //未进行数据的初始化
                    return -1;
                }
            }
            //走到这里说明数据完整,开始更新
            for(String key:tableMap.keySet()) {
                Table table = tableMap.get(key);
                String json = jedis.get(key);
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
                    result += deviceDao.updateStatus(map);
                }

                //更新完成后同步redis数据
                List<Device> list = deviceDao.queryAllData(table);
                String new_json = mapper.writeValueAsString(list);
                jedis.set(key,new_json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void startAutoUpdate(Integer time, String[] ids) {
        Timer timer = new Timer();   //定时器销毁就不能用了,要启用一个新的定时器
        this.timer = timer;
        init(ids);
        Integer msec = time * 60 * 1000;
        timer.schedule(new DeviceTimerTask(this,ids) , new Date() , msec);
    }

    public void closeAutoUpdate() {
        timer.cancel();
    }
}
