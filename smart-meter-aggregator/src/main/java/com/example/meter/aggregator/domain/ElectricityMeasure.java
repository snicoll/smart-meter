package com.example.meter.aggregator.domain;

import java.io.Serializable;
import java.time.Instant;

public final class ElectricityMeasure implements Serializable {

	private final String deviceId;

	private final String zoneId;

	private final Instant timestamp;

	private final float power;

	public ElectricityMeasure(String deviceId, String zoneId, Instant timestamp,
			float power) {
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
