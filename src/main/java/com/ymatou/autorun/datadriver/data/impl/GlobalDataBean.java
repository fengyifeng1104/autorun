package com.ymatou.autorun.datadriver.data.impl;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@ConfigurationProperties(prefix = "global",locations = "classpath:testdata.properties")  
public class GlobalDataBean {
	/*		global.userId=20336378
			global.userName=fyftest4
			global.adminUserName=testmn
			global.adminUserPswd=123123
			global.activityId=18946*/
	
	
	
	private String userId;
	private String activityId;
	private String userName;
	private String adminUserName;
	private String adminUserPswd;
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAdminUserName() {
		return adminUserName;
	}
	public void setAdminUserName(String adminUserName) {
		this.adminUserName = adminUserName;
	}
	public String getAdminUserPswd() {
		return adminUserPswd;
	}
	public void setAdminUserPswd(String adminUserPswd) {
		this.adminUserPswd = adminUserPswd;
	}
	
	

}
