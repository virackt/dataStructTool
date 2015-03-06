package com.nineLin.dataStructTool.file;

import com.nineLin.dataStructTool.core.file.SQLFileWriter;
import com.nineLin.dataStructTool.core.plugin.PropertiesPlugin;
import com.nineLin.dataStructTool.core.reader.ExcelReader;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Created by vic on 15-3-6.
 */
public class TestSqlFileWriter {

    @Test
    public void writeFileMap(){
        PropertiesPlugin plugin = new PropertiesPlugin();
        plugin.start();
        Map<String, List<String>> map = ExcelReader.readExcel(PropertiesPlugin.getStringValue("excelPath"));
        SQLFileWriter.getInstance().writeSqlMap2Files(map);
    }
}
