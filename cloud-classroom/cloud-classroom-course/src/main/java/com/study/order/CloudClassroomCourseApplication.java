package com.study.order;

//import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.study.security.auth.client.EnableClientAuthClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.study.order.mapper")
@EnableSwagger2
@RefreshScope
@EnableDiscoveryClient
@EnableAutoConfiguration
@EnableFeignClients({"com.study.security.auth.client.feign","com.study.order.client"})
@EnableClientAuthClient
public class CloudClassroomCourseApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudClassroomCourseApplication.class, args);

	}
}
