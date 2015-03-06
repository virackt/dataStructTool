package com.nineLin.dataStructTool.core.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * Created by vic on 15-3-6.
 */
public class DBUtil {
    private static final Logger log = LoggerFactory.getLogger("DBUtil");

    private int batchSize;

    public DBUtil(int batchSize) {
        this.batchSize = batchSize;
    }

    /**
     * operate a map of sql
     * @param conn
     * @param map
     */
    public void writeSQLMap(final Connection conn, Map<String, List<String>> map){
        Statement st = null;
        try {
            conn.setAutoCommit(false);
            st = conn.createStatement();
            for(Map.Entry<String, List<String>> entry : map.entrySet()){
                writeSqlList(conn, st, entry.getKey(), entry.getValue());
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            connectionRelease(conn, st);
        }
    }

    /**
     * insert a list of sql
     * @param st
     * @param tableName
     * @param sqlList
     */
    private void writeSqlList(final Connection conn, final Statement st, final String tableName, List<String> sqlList) throws SQLException{
        String sql;
        int count = 0;
        log.info("======start to execute sql, tableName: " + tableName);
        for (int i = 0; i < sqlList.size(); i++) {
            sql = sqlList.get(i);
            count ++;
            st.executeUpdate(sql);
            if(count >= batchSize){
                count = 0;
                st.executeBatch();
                conn.commit();
            }
        }
        conn.commit();
        sqlList.clear();
    }

    /**
     * release connection
     * @param conn
     * @param st
     */
    private void connectionRelease(Connection conn, Statement st){
        try{
            if(st != null){
                st.close();
                st = null;
            }
            if(conn != null){
                conn.close();
                conn = null;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
