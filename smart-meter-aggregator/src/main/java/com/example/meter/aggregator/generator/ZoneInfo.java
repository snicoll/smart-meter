package com.example.meter.aggregator.generator;

import java.util.Random;

public final class ZoneInfo {

	private static final Random random = new Random();

	private final String zoneId;

	private int devicesCount;

	private PowerRange powerRange;

	ZoneInfo(String zoneId, int devicesCount, float powerLow, float powerHigh) {
		this.zoneId = zoneId;
		this.devicesCount = devicesCount;
		this.powerRange = new PowerRange(powerLow, powerHigh);
	}

	public String getZoneId() {
		return this.zoneId;
	}

	public int getDevicesCount() {
		return this.devicesCount;
	}

	public PowerRange getPowerRange() {
		return this.powerRange;
	}

	public void updatePowerRange(Float powerLow, Float powerHigh) {
		float effectivePowerLow = (powerLow != null ? powerLow
				: this.powerRange.powerLow);
		float effectivePowerHigh = (powerHigh != null ? powerHigh
				: this.powerRange.powerHigh);
		this.powerRange = new PowerRange(effectivePowerLow, effectivePowerHigh);
	}

	public float randomPower() {
		PowerRange powerRange = this.powerRange;
		return powerRange.randomPower();
	}

	public static class PowerRange {

		private final float powerLow;

		private final float powerHigh;

		PowerRange(float powerLow, float powerHigh) {
			this.powerLow = powerLow;
			this.powerHigh = powerHigh;
		}

		public float getPowerLow() {
			return this.powerLow;
		}

		public float getPowerHigh() {
			return this.powerHigh;
		}

		float randomPower() {
			float delta = this.powerHigh - this.powerLow;
			return this.powerLow + (random.nextFloat() * delta);
		}

	}
}
