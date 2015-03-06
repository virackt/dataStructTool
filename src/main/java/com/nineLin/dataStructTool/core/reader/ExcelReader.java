package com.nineLin.dataStructTool.core.reader;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 读取excel文件
 * Created by vic on 15-3-4.
 */
public class ExcelReader {
    private static final Logger log = LoggerFactory.getLogger("excelReader");

    public static List<String> readExcel(final File file) {
        if (!file.isFile()) {
            log.warn(file.getName() + " is not a file!!!");
            return null;
        }
        XSSFWorkbook xssfWorkbook;
        try {
            xssfWorkbook = new XSSFWorkbook(new FileInputStream(file));
            final XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            Iterator<Row> it = sheet.rowIterator();
            int rowNum = 0;
            int cellNum = 0;
            final String fileName = file.getName();
            final String tableName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.lastIndexOf(".xlsx")).toLowerCase();
            final List<String> commentList = new ArrayList<String>();
            final List<String> typeList = new ArrayList<String>();
            final List<String> paramNameList = new ArrayList<String>();
            final List<Integer> keyList = new ArrayList<Integer>();
            final List<String> sqlList = new ArrayList<String>();
            while (it.hasNext()) {
                final Row row = it.next();
                if (row != null) {
                    rowNum++;
                    switch (rowNum){
                        case 1:// comment
                            getCommentFromRow(row, commentList);
                            break;
                        case 2:// parameter name
                            cellNum = getParamNamesFromRow(row, paramNameList);
                            break;
                        case 3:// parameter type
                            getParamTypesFromRow(row, cellNum, typeList);
                            break;
                        case 4:// primary key mark
                            getPrimaryKeyMarkFromRow(row, cellNum, keyList);
                            break;
                        default:// create insert sql
                            sqlList.add(getInsertSqlFromRow(row, cellNum, tableName, typeList));
                    }
                } else {
                    break;
                }
            }
            final String createTbSql = createTableSql(tableName, commentList, paramNameList, typeList, keyList);
            List<String> resultList = new ArrayList<String>();
            resultList.add("DROP TABLE IF EXISTS `" + tableName + "`;");
            resultList.add(createTbSql);
            resultList.addAll(sqlList);
            return resultList;

        } catch (IOException e) {
            e.printStackTrace();
            log.error("IoError!!! fileName: " + file.getName() + " \n" + e.getMessage());
        }
        return null;
    }

    /**
     * get comment from a row
     *
     * @param row
     * @param commentList
     */
    private static void getCommentFromRow(final Row row, final List<String> commentList) {
        final Iterator<Cell> cellIt = row.cellIterator();
        while (cellIt.hasNext()) {
            XSSFCell cell = (XSSFCell) cellIt.next();
            if (cell != null) {
                commentList.add(cell.getStringCellValue());
            } else {
                commentList.add("");
            }
        }
    }

    /**
     * get parameter names from a row
     * @param row
     * @param paramNameList
     * @return
     */
    private static int getParamNamesFromRow(final Row row, final List<String> paramNameList) {
        int cellNum = 0;
        final Iterator<Cell> cellIt = row.cellIterator();
        while (cellIt.hasNext()) {
            XSSFCell cell = (XSSFCell) cellIt.next();
            if (cell != null) {
                cellNum++;
                paramNameList.add(cell.getStringCellValue());
            } else {
                break;
            }
        }
        return cellNum;
    }

    /**
     * get parameter types from a row
     *
     * @param row
     * @param typeList
     */
    private static void getParamTypesFromRow(final Row row, final int cellNum, final List<String> typeList) {
        for (int i = 0; i < cellNum; i++) {
            final XSSFCell cell = (XSSFCell) row.getCell(i);
            if (cell == null) {
                throw new RuntimeException("parameter type is null! location:【3, " + (i + 1) + "】");
            }
            final String cellValue = cell.getStringCellValue();
            if (!cellValue.contains("int") && !cellValue.contains("varchar") && !cellValue.contains("float")) {
                throw new RuntimeException("parameter type not specified!!!location: 【3, " + (i + 1) + "】" + cellValue);
            } else {
                typeList.add(cellValue);
            }
        }
    }

    /**
     * get primary key indexes
     *
     * @param row
     * @param keyList
     */
    private static void getPrimaryKeyMarkFromRow(final Row row, final int cellNum, final List<Integer> keyList) {
        for (int i = 0; i < cellNum; i++) {
            final XSSFCell cell = (XSSFCell) row.getCell(i);
            final String cellValue = cell.getStringCellValue();
            if (cellValue.equals("key")) {
                keyList.add(i);
            }
        }
    }

    /**
     * get insert sql
     * @param row
     * @param cellNum
     * @param tableName
     * @param typeList
     * @return
     */
    private static String getInsertSqlFromRow(final Row row, final int cellNum, final String tableName, final List<String> typeList) {
        StringBuffer sb = new StringBuffer();
        sb.append("insert into ").append(tableName).append(" values(");
        for (int i = 0; i < cellNum; i++) {
            final XSSFCell cell = (XSSFCell) row.getCell(i);
            final String type = typeList.get(i);
            if(type.contains("int")){
                if (cell == null) {
                    sb.append(0);
                } else {
                    try {
                        sb.append((int) cell.getNumericCellValue());
                    } catch (NumberFormatException e) {
                        sb.append(Integer.parseInt(cell.getStringCellValue()));
                    }
                }
            } else if (type.contains("varchar")) {
                String cellValue = "";
                if (cell != null) {
                    cellValue = cell.getStringCellValue();
                }
                if (StringUtils.isEmpty(cellValue)) {
                    sb.append("''");
                } else {
                    sb.append("'" + cellValue + "'");
                }
            } else if (type.contains("float")) {
                sb.append((float) cell.getNumericCellValue());
            }
            if (i != cellNum - 1) {
                sb.append(",");
            }
        }
        sb.append(");");
        return sb.toString();
    }

    /**
     * build a table create sql
     *
     * @param tableName
     * @param commentList
     * @param paramNameList
     * @param typeList
     * @param keyList
     * @return
     */
    private static String createTableSql(String tableName, List<String> commentList, List<String> paramNameList, List<String> typeList, List<Integer> keyList) {
        StringBuffer sb = new StringBuffer();
        sb.append("create table " + tableName + "(");
        boolean isCommentCanUse = (commentList.size() == paramNameList.size());
        for (int i = 0; i < paramNameList.size(); i++) {
            String paramName = paramNameList.get(i);
            String typeName = typeList.get(i);
            sb.append("`" + paramName + "` " + typeName);
            if (isCommentCanUse) {
                sb.append(" comment '" + commentList.get(i) + "'");
            } else {
                sb.append(",");
            }
            sb.append("primary key(");
            for (int index : keyList) {
                sb.append(paramNameList.get(index) + ",");
            }
        }
        final String rtStr = sb.toString();
        return rtStr.substring(0, rtStr.length() - 1) + ")) ENGINE=MyIsAM DEFAULT CHARSET=utf8;";
    }

}
