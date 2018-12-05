package com.wolf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ShopMessageApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopMessageApplication.class, args);
    }
}