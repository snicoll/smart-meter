package com.example.meter.dashboard.sampling;

import java.time.Instant;

import com.example.meter.dashboard.generator.ElectricityMeasure;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "powergridsamples")
@TypeAlias("powergridsample")
public class PowerGridSample {

	@Id
	private String id;

	private long deviceCount;

	private String zoneId;

	private Instant timestamp;

	private float totalPower;

	public PowerGridSample() {
	}

	public PowerGridSample(String zoneId, Instant timestamp) {
		this.zoneId = zoneId;
		this.timestamp = timestamp;
	}

	public PowerGridSample(long deviceCount, String zoneId, Instant timestamp, float totalPower) {
		this.deviceCount = deviceCount;
		this.zoneId = zoneId;
		this.timestamp = timestamp;
		this.totalPower = totalPower;
	}

	public void addMeasure(ElectricityMeasure measure) {
		this.deviceCount++;
		this.totalPower += measure.getPower();
	}

	public String getId() {
		return id;
	}

	public long getDeviceCount() {
		return deviceCount;
	}

	public void setDeviceCount(long deviceCount) {
		this.deviceCount = deviceCount;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public float getTotalPower() {
		return totalPower;
	}

	public void setTotalPower(float totalPower) {
		this.totalPower = totalPower;
	}

	@Override
	public String toString() {
		return "PowerGridSample{" +
				"id='" + id + '\'' +
				", deviceCount=" + deviceCount +
				", zoneId='" + zoneId + '\'' +
				", timestamp=" + timestamp +
				", totalPower=" + totalPower +
				'}';
	}
}
