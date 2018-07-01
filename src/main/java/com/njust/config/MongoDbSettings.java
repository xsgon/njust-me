package com.njust.config;


import com.mongodb.MongoClientOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoDbSettings {

    @Bean
    public MongoClientOptions mongoOptions() {
        return MongoClientOptions.builder()
                .socketTimeout(2000)
                .minHeartbeatFrequency(1000)
                .heartbeatSocketTimeout(3000)
                .connectTimeout(30)
                .socketKeepAlive(true)
                .minConnectionsPerHost(1)
                .build();
    }

}