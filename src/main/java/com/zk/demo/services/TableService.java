package com.zk.demo.services;

import com.zk.demo.entities.Table;

import java.util.List;

/**
 * Created by Koori_Cc on 2017/9/4.
 */
public interface TableService {

    Integer addTable(Table table);

    List<Table> getTableList();

    Table getTableById(String id);

    Integer updateTable(Table table);

    Integer deleteTableById(String id);

}
