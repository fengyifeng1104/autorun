package com.ymatou.autorun.datadriver.face.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.ymatou.autorun.datadriver.data.conf.MongoDSconf;
import com.ymatou.autorun.datadriver.face.MongoSearch;

public class MongoSearchImpl implements MongoSearch{

	@Autowired 
	@Qualifier("mongoAA")
	private MongoTemplate mongoTemplate;

	private int resultLimit = MongoDSconf.Result_Limit;
	
	@Override
	public List<Map<String, Object>> selectBy(String dataSourceName, String collectionName, Map<String, Object> searchMap) {
		List<Map<String, Object>> ret = new ArrayList<Map<String,Object>>();
		int temp = 0;
		if (collectionName != null) {
			BasicDBObject queryObject = new BasicDBObject(searchMap);
			DBCollection collection = mongoTemplate.getCollection(collectionName);
			DBCursor querycursor = collection.find(queryObject).limit(resultLimit);
		}
		return ret;
	}

}
