package com.example.meter.dashboard.sampling;

import reactor.core.publisher.Flux;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

public interface PowerGridSampleRepository extends ReactiveSortingRepository<PowerGridSample, String> {

	Flux<PowerGridSample> findAllByZoneId(String zoneId, Pageable pageable);

}
