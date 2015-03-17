package com.nineLin.dataStructTool.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * 类查找
 * Created by vic on 15-3-4.
 */
public class ClassSeacher<T> {
    private Class<T> t;

    private String filePath;

    public ClassSeacher(Class<T> t, String filePath) {
        this.filePath = filePath;
        this.t = t;
    }

    /**
     * 在特定目录下获取子类
     * @return
     */
    public List<Class<? extends T>> findSonClasses() {
        File file = new File(PathUtil.ROOT_PATH + File.separator + filePath);
        if (file == null || !file.isDirectory()) {
            throw new RuntimeException("filepath error!!!! filePath = " + filePath);
        }
        String[] fileNames = file.list(getFileNameFilter());
        if (!filePath.endsWith("/")) {
            filePath = filePath + File.separator;
        }
        if (filePath.startsWith("/")) {
            filePath = filePath.substring(1);
        }
        return findSonClasses(fileNames);
    }

    /**
     * 在文件列表里面筛选获取对应类的子类
     * @param fileNames 文件列表
     * @return
     */
    public List<Class<? extends T>> findSonClasses(String[] fileNames) {
        List<Class<? extends T>> resultList = new ArrayList<Class<? extends T>>();
        try {
            for (String fileName : fileNames) {
                final String className = filePath + fileName.substring(0, fileName.indexOf(".class"));
                final Class clazz = Class.forName(className.replaceAll("/", "\\."));
                if (checkImplementClass(clazz) != null) {
                    resultList.add(clazz);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    /**
     * 检测某个类是否是指定类的子类
     * @param clazz
     * @return
     */
    public Class<?> checkImplementClass(Class<?> clazz) {
        if(clazz == null){
            return null;
        }
        if (t.isAssignableFrom(clazz) && !clazz.equals(t)) {
            return clazz;
        }
        return null;
    }

    public FilenameFilter getFileNameFilter(){
        return new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".class");
            }
        };
    }
}
