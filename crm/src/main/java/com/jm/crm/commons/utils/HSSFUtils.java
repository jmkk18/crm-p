package com.jm.crm.commons.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
/**
 * 关于excel文件操作的工具类
 */
public class HSSFUtils {
    /**
     * 从指定的HSSFCell对象中获取列的值
     * @return
     */
    public static String getCellValueForStr(HSSFCell cell){
        String str="";
        if(cell.getCellType()==HSSFCell.CELL_TYPE_STRING){
            str=cell.getStringCellValue();
        }else if(cell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
            str=cell.getNumericCellValue()+"";
        }else if(cell.getCellType()==HSSFCell.CELL_TYPE_FORMULA){
            str=cell.getCellFormula()+"";
        }else if(cell.getCellType()==HSSFCell.CELL_TYPE_BOOLEAN){
            str=cell.getBooleanCellValue()+"";
        }else {
            str="";
        }
        return str;
    }
}
