package com.ymatou.autorun.datadriver.face;

import java.util.List;
import java.util.Map;

public interface SqlSearch {
	 public List<Map<String,Object>> selectBy(String dataSourceName,String sqlStr);
}
