package com.pmitseas.gateway;

import com.pmitseas.gateway.lb.LeastConnLoadBalancerConfiguration;
import com.pmitseas.gateway.lb.RoundRobinLoadBalancerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;

@SpringBootApplication
@EnableDiscoveryClient
@LoadBalancerClients({ @LoadBalancerClient(value = "device-management-service", configuration = LeastConnLoadBalancerConfiguration.class),
		@LoadBalancerClient(value = "control-service", configuration = RoundRobinLoadBalancerConfiguration.class) })
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

}
