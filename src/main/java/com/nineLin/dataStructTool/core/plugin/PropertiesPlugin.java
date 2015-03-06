package com.nineLin.dataStructTool.core.plugin;

import com.nineLin.dataStructTool.core.reader.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * 读取配置文件
 * Created by vic on 15-3-4.
 */
public class PropertiesPlugin implements IPlugin {
    private static final Logger logger = LoggerFactory.getLogger("Plugin");

    private static Properties properties;

    @Override
    public boolean start() {
        properties = PropertiesReader.read("conf.properties");
        if(properties != null){
            logger.info("配置文件读取完成！！！");
            return true;
        }
        return false;
    }

    @Override
    public boolean stop() {
        return true;
    }


    public static String getStringValue(String key){
        return properties.getProperty(key);
    }

    public static String getStringWithDefaultValue(String key, String defaultValue){
        if(properties.containsKey(key)){
            return properties.getProperty(key);
        }
        return defaultValue;
    }

    public static int getIntValue(String key){
        String value = properties.getProperty(key);
        try{
            int result = Integer.parseInt(value);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("读取配置文件异常， key = " + key);
        }
    }

    public static int getIntValueWithDefault(String key, int defaultValue){
        if(properties.containsKey(key)){
            return Integer.parseInt(properties.getProperty(key));
        }
        return defaultValue;
    }

}
