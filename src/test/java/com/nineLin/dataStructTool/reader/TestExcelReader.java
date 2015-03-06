package com.nineLin.dataStructTool.reader;

import com.nineLin.dataStructTool.core.reader.ExcelReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * Created by vic on 15-3-5.
 */
public class TestExcelReader {

    @Test
    public void testRead(){
        File file = new File(TestExcelReader.class.getResource("/").getPath() + "itemTemplet.xlsx");
        List<String> list = ExcelReader.readExcel(file);
        Assert.assertNotNull(list);
    }

}
