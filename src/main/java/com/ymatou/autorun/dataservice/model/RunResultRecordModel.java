package com.ymatou.autorun.dataservice.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class RunResultRecordModel {
	@Id @GeneratedValue
	private Integer resultId;
	
	private String userName;
	
	private Integer passId;
	
	private String runRecordName;
	
	private String hostName;
	
	private Date addTime;

	public Integer getResultId() {
		return resultId;
	}

	public void setResultId(Integer resultId) {
		this.resultId = resultId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getPassId() {
		return passId;
	}

	public void setPassId(Integer passId) {
		this.passId = passId;
	}

	public String getRunRecordName() {
		return runRecordName;
	}

	public void setRunRecordName(String runRecordName) {
		this.runRecordName = runRecordName;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	
	
}
