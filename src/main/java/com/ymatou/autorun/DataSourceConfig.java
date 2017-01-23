package com.ymatou.autorun;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;



@Configuration
public class DataSourceConfig {
	
	public final static String IntegratedProductStr = "integratedproduct";
	public final static String YmtReleaseStr = "YmtRelease";
	public final static String AppProductReleaseStr = " AppProductRelease";
	
	
	
	
	
	
	@Primary
    @Bean(name = "DataDriverDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.DataDriverDataSource")
    public DataSource DataDriverDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "DataDriverJdbcTemplate")
    public JdbcTemplate DataDriverJdbcTemplate(@Qualifier("DataDriverDataSource")DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
    
    
    
    @Bean(name = "IntegratedProductDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.IntegratedProductDataSource")
    public DataSource IntegratedProductDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "IntegratedProductJdbcTemplate")
    public JdbcTemplate IntegratedProductJdbcTemplate(@Qualifier("IntegratedProductDataSource")DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
    
    
    @Bean(name = "YmtReleaseDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.YmtReleaseDataSource")
    public DataSource YmtReleaseDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "YmtReleaseJdbcTemplate")
    public JdbcTemplate YmtReleaseJdbcTemplate(@Qualifier("YmtReleaseDataSource")DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    
    @Bean(name = "AppProductReleaseDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.AppProductReleaseDataSource")
    public DataSource AppProductReleaseDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "AppProductReleaseJdbcTemplate")
    public JdbcTemplate AppProductReleaseJdbcTemplate(@Qualifier("AppProductReleaseDataSource")DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
    
    
    
}