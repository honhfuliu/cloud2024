package com.ziheng.cloud;


import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.ziheng.cloud.mapper")
public class Main8001 {
    public static void main(String[] args) {
        SpringApplication.run(Main8001.class, args);
    }
}
