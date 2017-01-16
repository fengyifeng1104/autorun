package com.ymatou.autorun.datadriver.face;

import java.util.Map;

public interface SqlSearch {
	 Map selectBy(Map<String, Object> searchMap);
}
