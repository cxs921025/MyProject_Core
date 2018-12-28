package com.cxs.core.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author ChenXS
 * 配置文件读取工具类
 */
public class PropertiesUtil {
    private static Properties properties = new Properties();

    /**
     * 不允许实例化
     */
    private PropertiesUtil() {
    }

    /**
     * 读取配置文件中的信息
     *
     * @param path 根路径开始的配置文件地址
     * @return Properties
     */
    public static Properties getPropertiesContext(String path) {
        FileInputStream inputStream;
        properties.clear();
        try {
            inputStream = new FileInputStream(PropertiesUtil.class.getResource("/").getPath() + path);
            properties.load(inputStream);
        } catch (IOException e) {
            LogUtil.error(e);
        }
        return properties;
    }
}
