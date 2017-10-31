package com.example.meter.dashboard.generator;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "zonedescriptors")
@TypeAlias("zonedescriptor")
public class ZoneDescriptor {

	@Id
	private String id;

	private String name;

	public ZoneDescriptor() {
	}

	ZoneDescriptor(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return "ZoneDescriptor{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
