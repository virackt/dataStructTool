package com.nineLin.dataStructTool.util;

import java.io.File;

/**
 * 路径工具类
 * Created by vic on 15-3-4.
 */
public class PathUtil {

    public static final String ROOT_PATH;
    public static final String CONF_PATH;
    static{
        ROOT_PATH = PathUtil.class.getResource(".").getPath().substring(0, PathUtil.class.getResource(".").getPath().indexOf("classes") + 7) + File.separator;
        CONF_PATH = ROOT_PATH + File.separator + "conf/";
    }

}
