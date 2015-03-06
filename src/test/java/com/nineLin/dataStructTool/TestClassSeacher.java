package com.nineLin.dataStructTool;

import com.nineLin.dataStructTool.util.ClassSeacher;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by vic on 15-3-4.
 */
public class TestClassSeacher {

    @Test
    public void testPlugin(){
        try {
            Class<?> clazz = Class.forName("com.nineLin.dataStructTool.core.plugin.IPlugin");
            ClassSeacher<?> classSeacher = new ClassSeacher(clazz, "/com/nineLin/dataStructTool/core/plugin/");
            List resultList = classSeacher.findSonClasses();
            Assert.assertEquals(resultList.size(), 1);
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
