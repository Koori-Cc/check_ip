package com.zk.demo.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zk.demo.daos.DeviceDao;
import com.zk.demo.entities.Device;
import com.zk.demo.services.DeviceService;
import com.zk.demo.utils.KeyUtils;
import com.zk.demo.utils.TelnetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Koori_Cc on 2017/8/30.
 */
@Service
public class DeviceServiceImpl implements DeviceService{


    @Autowired
    private DeviceDao deviceDao;

    private Logger logger = Logger.getLogger("DeviceServiceImpl");

    public boolean getData() {
        boolean result;

        try {

            List<Device> list = deviceDao.queryAllData();
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(list);

            Jedis jedis = new Jedis("127.0.0.1",6379);
            jedis.set(KeyUtils.DATA_DEVICE,json);
            result = true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.info("获取数据失败!");
            result = false;
        }

        return result;
    }

    public Integer updateStatus() {

        Jedis jedis = new Jedis("127.0.0.1",6379);
        String json = jedis.get(KeyUtils.DATA_DEVICE);
        Integer result = null;

        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Device> list = mapper.readValue(json, new TypeReference<List<Device>>() {});
            list = TelnetUtils.checkDeviceConnection(list);
            result = deviceDao.updateStatus(list);

            //更新完之后也要跟新redis中的数据
            list = deviceDao.queryAllData();
            json = mapper.writeValueAsString(list);
            jedis.set(KeyUtils.DATA_DEVICE,json);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }



}
