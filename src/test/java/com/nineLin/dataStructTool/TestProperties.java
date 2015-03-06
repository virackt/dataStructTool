package com.nineLin.dataStructTool;

import org.junit.Test;

/**
 * Created by vic on 15-3-4.
 */
public class TestProperties {

    @Test
    public void testReadFile(){
//        PropertiesReader.read("conf.properties");
//        Assert.assertEquals("/Users/vic/excel", PropertiesReader.getStringValue("excelPath"));
        try {
            Class<?> clazz = Class.forName("com.nineLin.dataStructTool.core.plugin.IPlugin");
            System.out.println(clazz.getName());
        }catch (Exception e){
            e.printStackTrace();
        }

//        PropertiesReader.getIntValue("test");
    }
}
