package com.zk.demo.entities;

import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Koori_Cc on 2017/9/1.
 */
public class Table {
    private String tableName;

    private static String field_id;
    private static String field_ip;
    private static String field_port;
    private static String field_status;

    static {
        ResourceBundle bundle = ResourceBundle.getBundle("config");
        field_id = bundle.getString("table.id");
        field_ip = bundle.getString("table.ip");
        field_port = bundle.getString("table.port");
        field_status = bundle.getString("table.status");
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getField_id() {
        return field_id;
    }

    public void setField_id(String field_id) {
        this.field_id = field_id;
    }

    public String getField_ip() {
        return field_ip;
    }

    public void setField_ip(String field_ip) {
        this.field_ip = field_ip;
    }

    public static String getField_port() {
        return field_port;
    }

    public static void setField_port(String field_port) {
        Table.field_port = field_port;
    }

    public String getField_status() {
        return field_status;
    }

    public void setField_status(String field_status) {
        this.field_status = field_status;
    }
}
