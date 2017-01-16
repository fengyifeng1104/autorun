package com.ymatou.autorun.datadriver.base.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.javassist.bytecode.stackmap.BasicBlock.Catch;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.sun.org.apache.bcel.internal.generic.RETURN;

public class ExcelUtil {
	/**
     * 读取Excel的内容，第一维数组存储的是一行中格列的值，二维数组存储的是多少个行
     * @param file 读取数据的源Excel
     * @param sheetIndex 
     * @return 读出的Excel中数据的内容
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String[][] getData(File file,int sheetIndex){
       try{
    	List<String[]> result = new ArrayList<String[]>();
       int rowSize = 0;
       BufferedInputStream in = new BufferedInputStream(new FileInputStream(
              file));
       // 打开HSSFWorkbook
       POIFSFileSystem fs = new POIFSFileSystem(in);
       HSSFWorkbook wb = new HSSFWorkbook(fs);
       HSSFCell cell = null;
       HSSFSheet st = wb.getSheetAt(sheetIndex);
	       for (int rowIndex = 0; rowIndex <= st.getLastRowNum(); rowIndex++) {
	          HSSFRow row = st.getRow(rowIndex);
	          if (row == null) {
	              continue;
	          }
	          int tempRowSize = row.getLastCellNum() + 1;
	          if (tempRowSize > rowSize) {
	              rowSize = tempRowSize;
	          }
	          String[] values = new String[rowSize];
	          Arrays.fill(values, "");
	          boolean hasValue = false;
	          for (short columnIndex = 0; columnIndex <= row.getLastCellNum(); columnIndex++) {
	              cell = row.getCell(columnIndex);
	              String value = "";
	              if (cell != null) {
	                  // 注意：一定要设成这个，否则可能会出现乱码
	                  
	                  switch (cell.getCellType()) {
	                  case HSSFCell.CELL_TYPE_STRING:
	                      value = cell.getStringCellValue();
	                      break;
	                  case HSSFCell.CELL_TYPE_NUMERIC:
	                      if (HSSFDateUtil.isCellDateFormatted(cell)) {
	                         Date date = cell.getDateCellValue();
	                         if (date != null) {
	                             value = new SimpleDateFormat("yyyy-MM-dd")
	                                    .format(date);
	                         } else {
	                             value = "";
	                         }
	                      } else {
	                         value = new DecimalFormat("0").format(cell
	                                .getNumericCellValue());
	                      }
	                      break;
	                  default:
	                      value = "";
	                  }
	              }
	         
	              if (columnIndex == 0 && value.trim().equals("")) {
	                 break;
	              }
	              values[columnIndex] = rightTrim(value);
	              hasValue = true;
	          }
	
	          if (hasValue) {
	              result.add(values);
	          }
	       }
       
	       in.close();
	       String[][] returnArray = new String[result.size()][rowSize];
	       for (int i = 0; i < returnArray.length; i++) {
	           returnArray[i] = (String[]) result.get(i);
	       }
	       return returnArray;
    
	    }catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	       return null;
}

    /**
     * 去掉字符串右边的空格
     * @param str 要处理的字符串
     * @return 处理后的字符串
     */
    public static String rightTrim(String str) {
       if (str == null) {
           return "";
       }
       int length = str.length();
       for (int i = length - 1; i >= 0; i--) {
           if (str.charAt(i) != 0x20) {
              break;
           }
           length--;
       }
       return str.substring(0, length);
    }
     
 	public static List<Map<String, String>> getDataAsMap(File file,int sheetIndex){
 		
 		List<Map<String,String>> mapList = new ArrayList <Map<String,String>>();

		String[][] ret;
		ret = getData(file,sheetIndex);
 		if (ret.length>1){
 			String[] colnames = ret[0];
 			
 			for(int i=1;i<ret.length;i++){
 				String[] rowValues = ret[i];
 				Map<String, String> rowValuesMap = new HashMap<String,String>();
 				for(int j=0;j<colnames.length;j++){
 					if (colnames[j] != null && !colnames[j].equals("")){
 						rowValuesMap.put(colnames[j], rowValues[j]);
 					}
 				}
 				mapList.add(rowValuesMap);
 			}
 		}
 		
 		return mapList;
 		
 	}
 	
 	
 	
 	
 	
     public static void main(String[] args) throws FileNotFoundException, IOException {
    	 File file = new File("C:\\Users\\fengyifeng\\Desktop\\test\\TestData.xls");
    	 String[][] result = getData(file, 0);
    	 List<Map<String,String>> mapList = getDataAsMap(file,0);
    	 System.out.println(result[0][0]);
	}
     
     
}
