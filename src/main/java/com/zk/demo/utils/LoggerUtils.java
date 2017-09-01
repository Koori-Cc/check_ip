package com.zk.demo.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Created by Koori_Cc on 2017/9/1.
 */
public class LoggerUtils {

    private static String path;
    private static String filename;

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static Logger logger = Logger.getLogger("LoggerUtils");

    //初始化存储路径
    static {
        ResourceBundle bundle = ResourceBundle.getBundle("config");
        path = bundle.getString("log.path");
        filename = bundle.getString("log.filename");
    }

    public static void log(String msg){
        String date = format.format(new Date());
        String info = date + " ==----==>  " + msg;

        File file = new File(path + File.separator + filename);
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file,true));   //追加!
            bw.append(info + "\r\n");
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("写入日志到文件出错");
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.info("关闭流资源出错");
            }
        }





    }

}
