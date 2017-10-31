package com.example.meter.dashboard;

import java.time.Duration;
import java.util.List;

import com.example.meter.dashboard.generator.ZoneDescriptor;
import com.example.meter.dashboard.sampling.PowerGridSample;
import com.mongodb.reactivestreams.client.MongoCollection;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class MongoCollectionInitializer implements SmartInitializingSingleton {

	private static final Logger logger = LoggerFactory.getLogger(MongoCollectionInitializer.class);

	private final ReactiveMongoTemplate mongoTemplate;

	private final DashboardProperties properties;

	public MongoCollectionInitializer(ReactiveMongoTemplate mongoTemplate,
			DashboardProperties properties) {
		this.mongoTemplate = mongoTemplate;
		this.properties = properties;
	}

	@Override
	public void afterSingletonsInstantiated() {
		logger.info("Initializing MongoDB store if necessary");
		Flux<ZoneDescriptor> zoneDescriptors = WebClient
				.create(properties.getGenerator().getServiceUrl())
				.get().uri("/actuator/zones")
				.retrieve().bodyToFlux(ZoneReport.class)
				.flatMapIterable(ZoneReport::getZones);

		// Retry if the remote service is not available yet
		Flux<ZoneDescriptor> resilientZoneDescriptors = zoneDescriptors.onErrorResume(ex ->
				zoneDescriptors.delaySubscription(Duration.ofSeconds(1)).retry(3));

		mongoTemplate.collectionExists(PowerGridSample.class)
				.filter(available -> !available).flatMap(i -> createSampleCollection())
				.then(mongoTemplate.dropCollection(ZoneDescriptor.class))
				.then(mongoTemplate.createCollection(ZoneDescriptor.class))
				.thenMany(resilientZoneDescriptors.flatMap(mongoTemplate::insert))
				.blockLast();
	}

	private Mono<MongoCollection<Document>> createSampleCollection() {
		CollectionOptions options = CollectionOptions.empty().size(104857600).capped();
		return mongoTemplate.createCollection(PowerGridSample.class, options);
	}

	private static class ZoneReport {
		private List<ZoneDescriptor> zones;

		public List<ZoneDescriptor> getZones() {
			return this.zones;
		}

		public void setZones(List<ZoneDescriptor> zones) {
			this.zones = zones;
		}

	}

}
