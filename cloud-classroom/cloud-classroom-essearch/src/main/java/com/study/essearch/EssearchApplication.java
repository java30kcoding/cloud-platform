package com.study.essearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@RefreshScope
@EnableDiscoveryClient
@EnableAutoConfiguration
//@EnableFeignClients
//@EnableApolloConfig
public class EssearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(EssearchApplication.class, args);
    }

}

