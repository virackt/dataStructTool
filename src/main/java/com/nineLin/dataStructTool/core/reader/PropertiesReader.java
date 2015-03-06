package com.nineLin.dataStructTool.core.reader;

import com.nineLin.dataStructTool.util.PathUtil;

import java.io.*;
import java.util.Properties;

/**
 * Created by vic on 15-3-4.
 */
public class PropertiesReader {

    public static Properties read(String fileName){
        Properties properties = null;
        InputStream is = null;
        try {
            is = PropertiesReader.class.getResourceAsStream(fileName);
            if(is == null){
                is = new FileInputStream(new File(PathUtil.CONF_PATH + File.separator + fileName));
            }
            properties = new Properties();
            properties.load(is);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return properties;
    }

}
