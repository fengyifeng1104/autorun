package com.ymatou.autorun.datadriver;

import java.io.File;

import com.ymatou.autorun.datadriver.data.GlobalData;
import com.ymatou.autorun.datadriver.data.ImportData;
import com.ymatou.autorun.datadriver.data.impl.GlobalDataFromExcelImpl;
import com.ymatou.autorun.datadriver.data.impl.ImportDataFromDBImpl;
import com.ymatou.autorun.datadriver.execute.helper.CaseExecute;

public class mainTest {

	public static void main(String[] args) {
		
		File file = new File("E:\\fyf\\Testing\\source\\YmatouTest\\resource\\TestData.xls");
		GlobalData globalData = new GlobalDataFromExcelImpl(file, 1);
		ImportData importData = new ImportDataFromDBImpl(10004);
		CaseExecute.executeAndCheck(importData,globalData);
	}

}
