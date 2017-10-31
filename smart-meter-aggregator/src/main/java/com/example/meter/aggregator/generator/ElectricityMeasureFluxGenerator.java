package com.example.meter.aggregator.generator;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;
import java.util.UUID;

import com.example.meter.aggregator.domain.ElectricityMeasure;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class ElectricityMeasureFluxGenerator {

	private static final Random random = new Random();

	private final int periodicity;

	private final ZoneInfo zoneInfo;

	private final String sensorIdPrefix;

	ElectricityMeasureFluxGenerator(Duration periodicity, ZoneInfo zoneInfo) {
		this.periodicity = (int) periodicity.getSeconds();
		this.zoneInfo = zoneInfo;
		this.sensorIdPrefix = UUID.randomUUID().toString().substring(0, 16);
	}

	public Flux<ElectricityMeasure> sensorData() {
		return Flux.interval(Duration.ZERO, Duration.ofSeconds(this.periodicity))
				.map(i -> generateReportTimestamp())
				.flatMap(reportTimestamp ->
						Flux.range(1, this.zoneInfo.getDevicesCount())
								.map(sensorIndex
										-> generateMeasure(sensorIndex, reportTimestamp))
								.delayUntil(i -> randomDelay()))
				.share();
	}

	private ElectricityMeasure generateMeasure(int sensorIndex, Instant reportTimestamp) {
		return new ElectricityMeasure(generateSensorId(sensorIndex), this.zoneInfo.getZoneId(),
				reportTimestamp, this.zoneInfo.randomPower());
	}

	private Mono<Long> randomDelay() {
		int max = (this.periodicity * 1000) / this.zoneInfo.getDevicesCount();
		return Mono.delay(Duration.ofMillis(10 + random.nextInt(max)));
	}

	private String generateSensorId(long sensorIndex) {
		return String.format("%s-%04d", this.sensorIdPrefix, sensorIndex);
	}

	private Instant generateReportTimestamp() {
		LocalDateTime now = LocalDateTime.now();
		return now.withSecond((now.getSecond() / this.periodicity) * this.periodicity)
				.withNano(0)
				.toInstant(ZoneOffset.UTC);
	}

}
