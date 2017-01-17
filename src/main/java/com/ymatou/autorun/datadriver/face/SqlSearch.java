package com.ymatou.autorun.datadriver.face;

import java.util.List;
import java.util.Map;

public interface SqlSearch {
	 List<Map<String,Object>> selectBy(Map<String, Object> searchMap);
}
