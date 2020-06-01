package com.pmitseas.devicemgmt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.DefaultSimpUserRegistry;

@SpringBootApplication
@EnableDiscoveryClient
public class DeviceMgmtApplication {

	@Autowired
	DefaultSimpUserRegistry defaultSimpUserRegistry;

	public static void main(String[] args) {
		SpringApplication.run(DeviceMgmtApplication.class, args);
	}

	@Endpoint(id="users")
	@Component
	public class UserCount {
		@ReadOperation
		public int getUserCount() {
			return defaultSimpUserRegistry.getUserCount();
		}
	}

}
