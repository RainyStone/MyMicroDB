package com.microdb.config;

import com.microdb.exception.ParseException;

import java.io.InputStream;
import java.util.Properties;

/**
 * Properties解析器
 */
public class PropertiesConfigParser  {
    /**
     * 解析配置
     *
     * @param filePath 配置文件路径
     */
    public DBConfig parse(String filePath){
        try {
            InputStream in=getClass().getResourceAsStream(filePath);
            Properties props=new Properties();
            props.load(in);
            int pageSize=Integer.parseInt(props.getProperty("page_size","4096"));
            int bufferPoolCapacity=Integer.parseInt(props.getProperty("buffer_pool_capacity", "100"));
            return new DBConfig(pageSize,bufferPoolCapacity);
        }catch (Exception e){
            throw new ParseException("配置解析失败",e);
        }
    }
}
