package com.ymatou.autorun.dataservice.model;

public class RunningDataModel {
	
	
	//这是发起测试请求的人员的姓名
	String reqUserName;
	
	//这是case的编号
	Integer caseId;
	
	//这是case的描述信息，描述此case是测什么的
	String caseDescription;
	
	//这是接口所属模块的名称，如 发布商品属于商品管理
	String modelDescription;
	
	//这是接口的名称，如发布商品，发布直播
	String sceneDescription;
	
	//这是接口的站点名称
	String sceneHost;
	
	//这是接口的完整的路由
	String sceneApi;
	
	//这是接口的请求类型
	String reqMethod;
	
	//这是接口请求的模版的名称
	String templateName;
	
	//这是接口的请求默认报文
	String templateDetail;

	//这是一类接口请求前需要额外做的事情，备用，下沉到接口层面，不是case层面，如发布直播需要将所有直播干掉
	String beforeToDo;
	
	//这是一类接口请求后需要额外做的事情，备用，下沉到接口层面，不是case层面
	String afterToDo;
	
	//这是接口请求后需要比对的的输入数据和SQL落地数据的对应关系，键值对
	String input2SqlMap;

	//这是接口请求后需要比对的的落地的SQL和落地的Mongo之间的对应关系，键值对
	String sql2MongoMap;
	
	//这是接口请求后需要比对的查询结果和SQL中数据的对应关系,键值对
	String output2SqlMap;
	
	//这是接口请求后需要比对的查询结果和Monog中数据的对应关系,键值对
	String output2MongoMap;
	
	//这是当前case额外需要输入的,或者更新的输入信息,
	String extraInputList;
	
	//这是当前case除了模版中需要检查的信息外,额外补充的检查信息
	String extraCheckList;
	
	//这是默认检查开关,用来控制是否检查模版中的检查点
	String defaultCheckType;

	public String getReqUserName() {
		return reqUserName;
	}

	public void setReqUserName(String reqUserName) {
		this.reqUserName = reqUserName;
	}

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public String getCaseDescription() {
		return caseDescription;
	}

	public void setCaseDescription(String caseDescription) {
		this.caseDescription = caseDescription;
	}

	public String getModelDescription() {
		return modelDescription;
	}

	public void setModelDescription(String modelDescription) {
		this.modelDescription = modelDescription;
	}

	public String getSceneDescription() {
		return sceneDescription;
	}

	public void setSceneDescription(String sceneDescription) {
		this.sceneDescription = sceneDescription;
	}

	public String getSceneHost() {
		return sceneHost;
	}

	public void setSceneHost(String sceneHost) {
		this.sceneHost = sceneHost;
	}

	public String getSceneApi() {
		return sceneApi;
	}

	public void setSceneApi(String sceneApi) {
		this.sceneApi = sceneApi;
	}

	public String getReqMethod() {
		return reqMethod;
	}

	public void setReqMethod(String reqMethod) {
		this.reqMethod = reqMethod;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateDetail() {
		return templateDetail;
	}

	public void setTemplateDetail(String templateDetail) {
		this.templateDetail = templateDetail;
	}

	public String getBeforeToDo() {
		return beforeToDo;
	}

	public void setBeforeToDo(String beforeToDo) {
		this.beforeToDo = beforeToDo;
	}

	public String getAfterToDo() {
		return afterToDo;
	}

	public void setAfterToDo(String afterToDo) {
		this.afterToDo = afterToDo;
	}

	public String getInput2SqlMap() {
		return input2SqlMap;
	}

	public void setInput2SqlMap(String input2SqlMap) {
		this.input2SqlMap = input2SqlMap;
	}

	public String getSql2MongoMap() {
		return sql2MongoMap;
	}

	public void setSql2MongoMap(String sql2MongoMap) {
		this.sql2MongoMap = sql2MongoMap;
	}

	public String getOutput2SqlMap() {
		return output2SqlMap;
	}

	public void setOutput2SqlMap(String output2SqlMap) {
		this.output2SqlMap = output2SqlMap;
	}

	public String getOutput2MongoMap() {
		return output2MongoMap;
	}

	public void setOutput2MongoMap(String output2MongoMap) {
		this.output2MongoMap = output2MongoMap;
	}

	public String getExtraInputList() {
		return extraInputList;
	}

	public void setExtraInputList(String extraInputList) {
		this.extraInputList = extraInputList;
	}

	public String getExtraCheckList() {
		return extraCheckList;
	}

	public void setExtraCheckList(String extraCheckList) {
		this.extraCheckList = extraCheckList;
	}

	public String getDefaultCheckType() {
		return defaultCheckType;
	}

	public void setDefaultCheckType(String defaultCheckType) {
		this.defaultCheckType = defaultCheckType;
	}
	

	

	
	
	
	
	

}
