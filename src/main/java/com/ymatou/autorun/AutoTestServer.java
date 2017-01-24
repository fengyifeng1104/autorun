package com.ymatou.autorun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.ymatou.autorun.datadriver.data.impl.GlobalDataBean;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class AutoTestServer   
{
	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters() {	
	FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
	FastJsonConfig fastJsonConfig = new FastJsonConfig();
	fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
	fastConverter.setFastJsonConfig(fastJsonConfig);
	HttpMessageConverter<?> converter = fastConverter;
	return new HttpMessageConverters(converter);
	    }
	
    public static void main( String[] args )
    {
        System.out.println( "Hello run!" );
        System.out.println( "Hello run!" );
        SpringApplication.run(AutoTestServer.class, args);
        
        
    }
    
}
