package com.pmitseas.gateway.lb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.reactive.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.reactive.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.reactive.Request;
import org.springframework.cloud.client.loadbalancer.reactive.Response;
import org.springframework.cloud.loadbalancer.core.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class LeastConnLoadBalancer implements ReactorServiceInstanceLoadBalancer {

	@Deprecated
	private ObjectProvider<ServiceInstanceSupplier> serviceInstanceSupplier;

	private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

	private String serviceId; // TODO: check if needed

	private AtomicInteger position = new AtomicInteger();

	private static final String DEFAULT_USERS_PATH = "/actuator/users";

	@Autowired
	private WebClient webClient;

	/**
	 * Constructor
	 *
	 * @param serviceInstanceListSupplierProvider the ServiceInstanceListSupplier
	 */
	public LeastConnLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId,
			int seedPosition) {
		this.serviceId = serviceId;
		this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
		this.position = new AtomicInteger(seedPosition);
	}

	public LeastConnLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId) {
		this(serviceInstanceListSupplierProvider, serviceId, new Random().nextInt(1000));
	}

	@Override
	public Mono<Response<ServiceInstance>> choose(Request request) {
		if (serviceInstanceListSupplierProvider != null) {
			ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider.getIfAvailable(NoopServiceInstanceListSupplier::new);
			return supplier.get().next()
					.flatMap(serviceInstances -> Flux.merge(serviceInstances.stream().map(this::getUsersCount).collect(Collectors.toUnmodifiableList()))
					.reduce((instanceUsersCount, instanceUsersCount2) ->
							instanceUsersCount.getUserCount() < instanceUsersCount2.getUserCount() ? instanceUsersCount : instanceUsersCount2))
					.map(instance -> new DefaultResponse(instance.getServiceInstance()));
		}
		ServiceInstanceSupplier supplier = this.serviceInstanceSupplier.getIfAvailable(NoopServiceInstanceSupplier::new);
		return supplier.get().collectList().map(this::getInstanceResponse);
	}

	private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances) {
		if (instances.isEmpty()) {
			// log.warn("No servers available for service: " + this.serviceId);
			return new EmptyResponse();
		}

		int pos = Math.abs(this.position.incrementAndGet());
		ServiceInstance instance = instances.get(pos % instances.size());

		return new DefaultResponse(instance);
	}

	protected Mono<InstanceUsersCount> getUsersCount(ServiceInstance serviceInstance) {
		return webClient.get()
				.uri(UriComponentsBuilder.fromUri(serviceInstance.getUri()).path(DEFAULT_USERS_PATH).build().toUri())
				.exchange()
				.flatMap(clientResponse -> {
					if (clientResponse.statusCode().isError()) {
						log.error("Failed to retrieve number of users for instance {}. Error status {}", serviceInstance.getInstanceId(),
								clientResponse.statusCode());
						clientResponse.releaseBody();
						return Mono.just(new InstanceUsersCount(serviceInstance, Integer.MAX_VALUE));
					} else
						return clientResponse.bodyToMono(Integer.class).map(response -> new InstanceUsersCount(serviceInstance, response));
				});
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	private static class InstanceUsersCount {
		private ServiceInstance serviceInstance;
		private int userCount;
	}

}
