package com.nineLin.dataStructTool.core.db;

import com.nineLin.dataStructTool.core.plugin.PropertiesPlugin;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by vic on 15-3-6.
 */
public class SQLConnector {

    /**
     * get mysql connection
     * @return
     */
    public static Connection getConnection(){
        final String userName = PropertiesPlugin.getStringValue("userName");
        final String password = PropertiesPlugin.getStringValue("password");
        final String url = PropertiesPlugin.getStringValue("url");
        try{
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(url, userName, password);
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        } catch (SQLException e1){
            e1.printStackTrace();
        }
        return null;
    }

}
