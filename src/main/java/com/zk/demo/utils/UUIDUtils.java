package com.zk.demo.utils;

import java.util.UUID;

/**
 * Created by Koori_Cc on 2017/9/4.
 */
public class UUIDUtils {
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
