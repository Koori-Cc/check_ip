package com.zk.demo.daos;

import com.zk.demo.entities.Device;
import com.zk.demo.entities.Table;

import java.util.List;
import java.util.Map;

/**
 * Created by Koori_Cc on 2017/8/30.
 */
public interface DeviceDao {

    List<Device> queryAllData(Table table);

    Integer updateStatus(Map<Object,Object> map);



}
