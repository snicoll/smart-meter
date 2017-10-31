package com.example.meter.aggregator.management;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.example.meter.aggregator.generator.ElectricityMeasureGenerator;
import com.example.meter.aggregator.generator.ElectricityMeasureGeneratorProperties;
import com.example.meter.aggregator.generator.ZoneInfo;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "zones")
public class ZonesEndpoint {

	private final ElectricityMeasureGenerator electricityMeasureGenerator;
	private final ElectricityMeasureGeneratorProperties properties;

	public ZonesEndpoint(ElectricityMeasureGenerator electricityMeasureGenerator,
			ElectricityMeasureGeneratorProperties properties) {
		this.electricityMeasureGenerator = electricityMeasureGenerator;
		this.properties = properties;
	}

	@ReadOperation
	public ZoneReport zones() {
		Collection<ZoneDescriptor> descriptors = new ArrayList<>();
		this.electricityMeasureGenerator.getZones().forEach((id, zi) -> {
			descriptors.add(toZoneDescriptor(id, zi));
		});
		return new ZoneReport(descriptors);
	}

	@ReadOperation
	public ZoneDescriptor zone(@Selector String id) {
		ZoneInfo zoneInfo = this.electricityMeasureGenerator.getZones().get(id);
		if (zoneInfo != null) {
			return toZoneDescriptor(id, zoneInfo);
		}
		return null;
	}

	@WriteOperation
	public void updatePowerRange(@Selector String id,
			@Nullable Float powerLow, @Nullable Float powerHigh) {
		this.electricityMeasureGenerator.updatePowerRangeFor(id, powerLow, powerHigh);
	}

	private ZoneDescriptor toZoneDescriptor(String id, ZoneInfo zoneInfo) {
		ZoneInfo.PowerRange powerRange = zoneInfo.getPowerRange();
		return new ZoneDescriptor(id, this.properties.getZones().get(id).getName(),
				powerRange.getPowerLow(), powerRange.getPowerHigh());
	}

	public static class ZoneReport {

		private final Collection<ZoneDescriptor> zones;

		ZoneReport(Collection<ZoneDescriptor> zones) {
			this.zones = zones;
		}

		public Collection<ZoneDescriptor> getZones() {
			return this.zones;
		}
	}

	public static class ZoneDescriptor {

		private final String id;
		private final String name;
		private final float powerLow;
		private final float powerHigh;

		ZoneDescriptor(String id, String name, float powerLow,
				float powerHigh) {
			this.id = id;
			this.name = name;
			this.powerLow = powerLow;
			this.powerHigh = powerHigh;
		}

		public String getId() {
			return this.id;
		}

		public String getName() {
			return this.name;
		}

		public float getPowerLow() {
			return this.powerLow;
		}

		public float getPowerHigh() {
			return this.powerHigh;
		}

	}

}
