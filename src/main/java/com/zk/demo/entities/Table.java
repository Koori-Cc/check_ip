package com.zk.demo.entities;

import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Koori_Cc on 2017/9/1.
 */
public class Table {
    private String id;
    private String tableName;

    private String field_id;
    private String field_ip;
    private String field_port;
    private String field_status;
    private String field_type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getField_type() {
        return field_type;
    }

    public void setField_type(String field_type) {
        this.field_type = field_type;
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

    public String getField_port() {
        return field_port;
    }

    public void setField_port(String field_port) {
        this.field_port = field_port;
    }

    public String getField_status() {
        return field_status;
    }

    public void setField_status(String field_status) {
        this.field_status = field_status;
    }
}
