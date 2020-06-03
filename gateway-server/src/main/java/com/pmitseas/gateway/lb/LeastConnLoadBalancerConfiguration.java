package com.pmitseas.gateway.lb;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.loadbalancer.cache.LoadBalancerCacheManager;
import org.springframework.cloud.loadbalancer.core.CachingServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.DiscoveryClientServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class LeastConnLoadBalancerConfiguration {
	@Bean
	WebClient webClient() {
		return WebClient.builder().defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	}

	// TODO: maybe the following is not necessary unless we create our own delegate
	// @Bean
	ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(ReactiveDiscoveryClient discoveryClient, Environment environment,
			ApplicationContext context) {

		var delegate = new DiscoveryClientServiceInstanceListSupplier(discoveryClient, environment);

		ObjectProvider<LoadBalancerCacheManager> cacheManagerProvider = context.getBeanProvider(LoadBalancerCacheManager.class);
		if (cacheManagerProvider.getIfAvailable() != null) {
			return new CachingServiceInstanceListSupplier(delegate, cacheManagerProvider.getIfAvailable());
		}

		return delegate;
	}

	/**
	 * See org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientConfiguration#reactorServiceInstanceLoadBalancer(org.springframework.core.env.Environment, org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory)
	 */
	@Bean
	@Lazy
	public ReactorLoadBalancer<ServiceInstance> leastConn(Environment environment, LoadBalancerClientFactory loadBalancerClientFactory) {
		String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
		return new LeastConnLoadBalancer(loadBalancerClientFactory.getProvider(name, ServiceInstanceListSupplier.class), name);
	}

}
