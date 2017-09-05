package com.zk.demo.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zk.demo.daos.DeviceDao;
import com.zk.demo.daos.TableDao;
import com.zk.demo.entities.Device;
import com.zk.demo.entities.Table;
import com.zk.demo.services.TableService;
import com.zk.demo.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.logging.Logger;

/**
 * Created by Koori_Cc on 2017/9/4.
 */
@Service
public class TableServiceImpl implements TableService {

    @Autowired
    private TableDao tableDao;

    @Autowired
    private DeviceDao deviceDao;

    private Logger logger = Logger.getLogger("TableServiceImpl");

    private ResourceBundle bundle = ResourceBundle.getBundle("config");

    //定义Jedis成员变量
    private Jedis jedis;

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


    public Integer addTable(Table table) {
        String id = UUIDUtils.getUUID();
        table.setId(id);
        Integer result = tableDao.insertTable(table);
        return result;
    }

    public List<Table> getTableList() {
        return tableDao.queryTableList();
    }

    public Table getTableById(String id) {
        return tableDao.queryTableById(id);
    }

    public Integer updateTable(Table table) {
        Integer resule = tableDao.updateTable(table);
        return resule;
    }

    public Integer deleteTableById(String id) {
        Integer result = tableDao.deleteTableById(id);
        return result;
    }




}
