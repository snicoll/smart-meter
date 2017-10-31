package com.example.meter.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(DashboardProperties.class)
public class SmartMeterDashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartMeterDashboardApplication.class, args);
	}

}
