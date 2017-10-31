package com.example.meter.dashboard.web;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.example.meter.dashboard.DashboardProperties;
import com.example.meter.dashboard.generator.ZoneDescriptorRepository;
import com.example.meter.dashboard.sampling.PowerGridSample;
import com.example.meter.dashboard.sampling.PowerGridSampleRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.result.view.Rendering;

@Controller
public class DashboardController {

	private final int historySize;

	private final PowerGridSampleRepository powerGridSampleRepository;

	private final ZoneDescriptorRepository zoneDescriptorRepository;

	public DashboardController(DashboardProperties properties,
			PowerGridSampleRepository powerGridSampleRepository,
			ZoneDescriptorRepository zoneDescriptorRepository) {
		this.historySize = properties.getRenderer().getHistorySize();
		this.powerGridSampleRepository = powerGridSampleRepository;
		this.zoneDescriptorRepository = zoneDescriptorRepository;
	}

	@GetMapping("/")
	public Rendering home() {
		return Rendering
				.view("index")
				.modelAttribute("zones", this.zoneDescriptorRepository.findAll())
				.build();
	}

	@GetMapping("/zones/{zoneId}")
	public Mono<Rendering> displayZone(@PathVariable String zoneId) {
		PageRequest pageRequest = PageRequest.of(0, this.historySize,
				Sort.by("timestamp").descending());
		Flux<PowerGridSample> latestSamples = this.powerGridSampleRepository
				.findAllByZoneId(zoneId, pageRequest);

		return this.zoneDescriptorRepository.findById(zoneId)
				.switchIfEmpty(Mono.error(new MissingDataException(zoneId)))
				.map(zoneDescriptor -> Rendering
						.view("zone")
						.modelAttribute("zone", zoneDescriptor)
						.modelAttribute("samples", latestSamples)
						.build());
	}

	@GetMapping(path = "/zones/{zoneId}/updates", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	@ResponseBody
	public Flux<PowerGridSample> streamUpdates(@PathVariable String zoneId) {
		Instant startup = LocalDateTime.now().withSecond(0).toInstant(ZoneOffset.UTC);
		return this.powerGridSampleRepository
				.findWithTailableCursorByZoneIdAndTimestampAfter(zoneId, startup);
	}

}