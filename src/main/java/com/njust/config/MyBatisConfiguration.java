package com.njust.config;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
//@ConfigurationProperties(prefix = "spring.datasource")
@Data
@Log4j2
public class MyBatisConfiguration {
	@Value("${spring.datasource.url}")
	private String url;
	@Value("${spring.datasource.driverClassName}")
	private String driverClassName;
	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;
	@Bean(name = "dataSource")
	public DriverManagerDataSource dataSource() {
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setDriverClassName(driverClassName);
		driverManagerDataSource.setUrl(url);
		driverManagerDataSource.setUsername(username);
		driverManagerDataSource.setPassword(password);

		log.info("data source: " + url);
		return driverManagerDataSource;
	}

}
