package com.nineLin.dataStructTool.bootrap;

import com.nineLin.dataStructTool.core.db.DBUtil;
import com.nineLin.dataStructTool.core.db.SQLConnector;
import com.nineLin.dataStructTool.core.file.SQLFileWriter;
import com.nineLin.dataStructTool.core.plugin.IPlugin;
import com.nineLin.dataStructTool.core.plugin.PropertiesPlugin;
import com.nineLin.dataStructTool.core.reader.ExcelReader;
import com.nineLin.dataStructTool.util.ClassSeacher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by vic on 15-3-4.
 */
public class InitServer {
    private static final Logger log = LoggerFactory.getLogger("main");

    public static void main(String[] args){
        initPlugins();
        Map<String, List<String>> map = ExcelReader.readExcel(PropertiesPlugin.getStringValue("excelPath"));
        SQLFileWriter.getInstance().writeSqlMap2Files(map);
        DBUtil dbUtil = new DBUtil(PropertiesPlugin.getIntValueWithDefault("batchSize", 200));
        dbUtil.writeSQLMap(SQLConnector.getConnection(), map);
    }

    public static void initPlugins(){
        final String pluginClassPath = "com/nineLin/dataStructTool/core/plugin/";
        try {
            Class clazz = Class.forName("com.nineLin.dataStructTool.core.plugin.IPlugin");
            ClassSeacher<IPlugin> classSeacher = new ClassSeacher<IPlugin>(clazz, pluginClassPath);
            List<Class<? extends IPlugin>> resultList = classSeacher.findSonClasses();
            for(Class<? extends IPlugin> cls : resultList){
                IPlugin plugin = cls.newInstance();
                if(!plugin.start()){
                    log.error("plugin init error: pluginName: " + cls.getSimpleName());
                }
            }
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
