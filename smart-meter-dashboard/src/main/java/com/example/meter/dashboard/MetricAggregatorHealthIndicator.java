package com.example.meter.dashboard;

import reactor.core.publisher.Mono;

import org.springframework.boot.actuate.health.AbstractReactiveHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

@Component
class MetricAggregatorHealthIndicator extends AbstractReactiveHealthIndicator {

	private final WebClient webClient;

	private MetricAggregatorHealthIndicator(DashboardProperties properties,
			WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder
				.baseUrl(properties.getGenerator().getServiceUrl()).build();
	}

	@Override
	protected Mono<Health> doHealthCheck(Health.Builder builder) {
		return webClient.get().uri("/actuator/health")
				.exchange()
				.map(ClientResponse::statusCode)
				.map((status -> status.is2xxSuccessful() ? Health.up() : Health.down()))
				.map(Health.Builder::build);
	}

}
