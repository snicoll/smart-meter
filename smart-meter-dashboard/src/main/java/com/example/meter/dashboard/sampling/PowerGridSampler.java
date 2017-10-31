package com.example.meter.dashboard.sampling;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

import com.example.meter.dashboard.generator.ElectricityMeasure;
import com.example.meter.dashboard.generator.MeasuresCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PowerGridSampler implements ApplicationRunner, DisposableBean {

	private static final Logger logger = LoggerFactory.getLogger(PowerGridSampler.class);

	private final MeasuresCollector measuresCollector;

	private final PowerGridSampleRepository repository;

	private final AtomicReference<Instant> currentTimestamp = new AtomicReference<>(Instant.now());

	private Disposable subscription;


	public PowerGridSampler(MeasuresCollector measuresCollector,
			PowerGridSampleRepository repository) {
		this.measuresCollector = measuresCollector;
		this.repository = repository;
	}

	private Flux<PowerGridSample> sampleMeasuresForPowerGrid() {
		Flux<PowerGridSample> samples = measuresCollector.getElectricityMeasures()
				// buffer until the timestamp of the measures changes
				.windowUntil(timestampBoundaryTrigger(), true)
				// group measures by zoneIds + timestamp
				.flatMap(window -> window.groupBy(measure ->
						new PowerGridSampleKey(measure.getZoneId(), measure.getTimestamp())))
				// for each group, reduce all measures into a PowerGrid sample for that timestamp
				.flatMap(windowForZone -> {
					PowerGridSampleKey key = windowForZone.key();
					PowerGridSample initial = new PowerGridSample(key.zoneId, key.timestamp);
					return windowForZone.reduce(initial,
							(powerGridSample, measure) -> {
								powerGridSample.addMeasure(measure);
								return powerGridSample;
							});
				});

		// save the generated samples and return them
		return this.repository.saveAll(samples);
	}

	private Predicate<ElectricityMeasure> timestampBoundaryTrigger() {
		return measure -> {
			if (this.currentTimestamp.get().isBefore(measure.getTimestamp())) {
				this.currentTimestamp.set(measure.getTimestamp());
				return true;
			}
			return false;
		};
	}

	@Override
	public void run(ApplicationArguments args) {
		logger.info("Starting subscription with aggregator service");
		this.subscription = sampleMeasuresForPowerGrid().subscribe();
	}

	@Override
	public void destroy() {
		this.subscription.dispose();
	}

}
