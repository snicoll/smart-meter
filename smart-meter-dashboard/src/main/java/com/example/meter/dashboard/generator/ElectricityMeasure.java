package com.example.meter.dashboard.generator;

import java.io.Serializable;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class ElectricityMeasure implements Serializable {

	private final String deviceId;

	private final String zoneId;

	private final Instant timestamp;

	private final float power;

	@JsonCreator
	public ElectricityMeasure(@JsonProperty("deviceId") String deviceId,
			@JsonProperty("zoneId") String zoneId,
			@JsonProperty("timestamp") Instant timestamp,
			@JsonProperty("power") float power) {
		this.deviceId = deviceId;
		this.zoneId = zoneId;
		this.timestamp = timestamp;
		this.power = power;
	}

	public String getDeviceId() {
		return this.deviceId;
	}

	public String getZoneId() {
		return this.zoneId;
	}

	public Instant getTimestamp() {
		return this.timestamp;
	}

	public float getPower() {
		return this.power;
	}

}
