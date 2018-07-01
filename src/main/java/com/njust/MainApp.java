package com.njust;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.*;

@SpringBootApplication
@EnableGlobalMethodSecurity(jsr250Enabled = true)
//@EnableJpaRepositories(basePackages ={ "com.njust.repo"})
//@EnableTransactionManagement
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EntityScan(basePackages ={ "com.njust.model"})
public class MainApp {
	public static void main(String[] args) throws Exception {
		new SpringApplication(MainApp.class).run(args);
	}
}



/*
SpringBoot Notes

@Bean      :tells Spring 'here is an instance of this class, please keep hold of it and give it back to me when I ask'.
@Autowired :says 'please give me an instance of this class, for example, one that I created with an @Bean annotation earlier'.

*/
