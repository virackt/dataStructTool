package com.nineLin.dataStructTool.core.file;

import com.nineLin.dataStructTool.core.plugin.PropertiesPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

/**
 * write sql files
 * Created by vic on 15-3-6.
 */
public class SQLFileWriter {

    private static final Logger log = LoggerFactory.getLogger("sqlFileWriter");

    // sqlFile path
    private String filePath;


    private SQLFileWriter() {
        this.filePath = PropertiesPlugin.getStringValue("sqlFilePath");
    }

    private static SQLFileWriter writer = new SQLFileWriter();

    public static SQLFileWriter getInstance() {
        return writer;
    }

    /**
     * write a map of sql into files
     *
     * @param sqlMap
     */
    public void writeSqlMap2Files(final Map<String, List<String>> sqlMap) {
        File path = new File(filePath);
        if (path == null || !path.isDirectory()) {
            return;
        }
        for (Map.Entry<String, List<String>> entry : sqlMap.entrySet()) {
            writeSqlList2File(entry.getKey(), entry.getValue());
        }
    }

    /**
     * write a list of sql into one file
     *
     * @param tableName
     * @param sqlList
     */
    public void writeSqlList2File(final String tableName, final List<String> sqlList) {
        final String fileName = new StringBuffer(tableName).append(".sql").toString();
        File file = new File(filePath + fileName);
        FileOutputStream fos = null;
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            fos = new FileOutputStream(file);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            for (String sql : sqlList) {
                for (char c : sql.toCharArray()) {
                    buffer.putChar(c);
                }
                buffer.putChar('\r');
                buffer.putChar('\n');
            }
            buffer.flip();
            fos.getChannel().write(buffer);
            log.info(fileName + " file writing is finish...");
        } catch (IOException e) {
            log.error(fileName + "writing error!!!");
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.getChannel().close();
                    fos.close();
                    fos = null;
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

}
