package com.ymatou.autorun;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "run") 
public class AppRunConf {
	
	private String env;
	
	private String resultPath;

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getResultPath() {
		return resultPath;
	}

	public void setResultPath(String resultPath) {
		this.resultPath = resultPath;
	}
	
	
	
}
