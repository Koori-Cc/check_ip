package com.zk.demo.daos;

import com.zk.demo.entities.Table;

import java.util.List;

/**
 * Created by Koori_Cc on 2017/9/4.
 */
public interface TableDao {

    Integer insertTable(Table table);

    List<Table> queryTableList();

    Table queryTableById(String id);

    Integer updateTable(Table table);

    Integer deleteTableById(String id);

    List<Table> queryTableListByIds(String[] ids);

    List<String> queryAllTableName();

}
