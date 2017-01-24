package com.ymatou.autorun.datadriver.data.conf;

import java.net.UnknownHostException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;



@Configuration
public class MongoDSconf {
	public static int Result_Limit = 50;

	
	
	@Bean(name = "mongoAA")
	public MongoTemplate mongoTemplate(){
		try {
			MongoClient mongoClient = new MongoClient(new ServerAddress("172.16.101.169",27017));
			MongoDbFactory factory = new SimpleMongoDbFactory(mongoClient, "YmtBuyerProduct",new UserCredentials("BuyerProductuser","123456"));
			MongoTemplate aa= new MongoTemplate(factory);
			return aa;
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

		return null;
		
		
	}
	
	
    
    
}