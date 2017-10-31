package com.example.meter.dashboard.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class MissingDataException extends ResponseStatusException {

	private final String zoneId;

	public MissingDataException(String zoneId) {
		super(HttpStatus.NOT_FOUND, "Missing power data for Zone " + zoneId);
		this.zoneId = zoneId;
	}

	public String getZoneId() {
		return zoneId;
	}
}
