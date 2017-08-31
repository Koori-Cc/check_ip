package com.zk.demo.daos;

import com.zk.demo.entities.Device;

import java.util.List;

/**
 * Created by Koori_Cc on 2017/8/30.
 */
public interface DeviceDao {

    List<Device> queryAllData();

    Integer updateStatus(List<Device> list);



}
