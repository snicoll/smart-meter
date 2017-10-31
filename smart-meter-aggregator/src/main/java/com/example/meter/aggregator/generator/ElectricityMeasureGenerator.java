package com.example.meter.aggregator.generator;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.example.meter.aggregator.domain.ElectricityMeasure;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class ElectricityMeasureGenerator {

	private final Map<String, ZoneInfo> zones;
	private final Duration periodicity;

	public ElectricityMeasureGenerator(ElectricityMeasureGeneratorProperties properties) {
		this.zones = extractConfiguration(properties);
		this.periodicity = properties.getPeriodicity();
	}

	public Map<String, Flux<ElectricityMeasure>> generateSensorData() {
		Map<String, Flux<ElectricityMeasure>> content = new HashMap<>();
		this.zones.forEach((id, zone) -> {
			ElectricityMeasureFluxGenerator generator = new ElectricityMeasureFluxGenerator(
					this.periodicity, zone);
			content.put(id, generator.sensorData());
		});
		return content;
	}

	public Map<String, ZoneInfo> getZones() {
		return Collections.unmodifiableMap(this.zones);
	}

	public void updatePowerRangeFor(String zoneId, Float powerLow, Float powerHigh) {
		ZoneInfo zoneInfo = this.zones.get(zoneId);
		Assert.notNull(zoneInfo, "Zone with id " + zoneId + " does not exist");
		zoneInfo.updatePowerRange(powerLow, powerHigh);
	}

	private static Map<String, ZoneInfo> extractConfiguration(
			ElectricityMeasureGeneratorProperties properties) {
		Map<String, ZoneInfo> zones = new HashMap<>();
		properties.getZones().forEach((id, zone) -> zones.put(id,
				new ZoneInfo(id, zone.getDevicesCount(), zone.getPowerLow(), zone.getPowerHigh())));
		return zones;
	}
}
