package com.zk.demo.services;

import com.zk.demo.entities.Device;
import com.zk.demo.entities.Table;

import java.io.IOException;
import java.util.List;

/**
 * Created by Koori_Cc on 2017/8/30.
 */
public interface DeviceService {

    boolean getData(Table table);


    Integer updateStatus(Table table);

    List<String> getTableName();

}
