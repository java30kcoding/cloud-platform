package com.study.serviceadmin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@EnableAdminServer
@SpringBootApplication
@RefreshScope
@EnableDiscoveryClient
@EnableAutoConfiguration
public class ServiceAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run( ServiceAdminApplication.class, args );
    }

}



