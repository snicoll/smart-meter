package com.example.meter.dashboard;

import javax.validation.constraints.Max;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("meter.dashboard")
@Validated
public class DashboardProperties {

	@NestedConfigurationProperty
	private final Generator generator = new Generator();

	@NestedConfigurationProperty
	private final Renderer renderer = new Renderer();

	public Generator getGenerator() {
		return generator;
	}

	public Renderer getRenderer() {
		return this.renderer;
	}

	public static class Generator {

		private String serviceUrl = "http://localhost:8081";

		public String getServiceUrl() {
			return serviceUrl;
		}

		public void setServiceUrl(String serviceUrl) {
			this.serviceUrl = serviceUrl;
		}
	}

	@Validated
	public static class Renderer {

		@Max(40)
		private int historySize = 40;

		public int getHistorySize() {
			return this.historySize;
		}

		public void setHistorySize(int historySize) {
			this.historySize = historySize;
		}
		

	}

}
