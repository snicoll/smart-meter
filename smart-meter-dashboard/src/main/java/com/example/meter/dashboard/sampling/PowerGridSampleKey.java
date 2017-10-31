package com.example.meter.dashboard.sampling;

import java.time.Instant;

class PowerGridSampleKey {

	public final String zoneId;

	public final Instant timestamp;

	PowerGridSampleKey(String zoneId, Instant timestamp) {
		this.zoneId = zoneId;
		this.timestamp = timestamp;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PowerGridSampleKey that = (PowerGridSampleKey) o;

		if (!zoneId.equals(that.zoneId)) return false;
		return timestamp.equals(that.timestamp);
	}

	@Override
	public int hashCode() {
		int result = zoneId.hashCode();
		result = 31 * result + timestamp.hashCode();
		return result;
	}
}
