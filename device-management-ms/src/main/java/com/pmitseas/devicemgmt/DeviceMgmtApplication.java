package com.pmitseas.devicemgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class DeviceMgmtApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeviceMgmtApplication.class, args);
    }

}
