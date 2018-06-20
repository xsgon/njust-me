package com.njust;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.transaction.annotation.*;

@SpringBootApplication
//@EnableJpaRepositories(basePackages ={ "com.njust.repo"})
@EntityScan(basePackages ={ "com.njust.model"})
@EnableTransactionManagement
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
