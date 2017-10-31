package com.example.meter.dashboard.generator;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ZoneDescriptorRepository extends ReactiveMongoRepository<ZoneDescriptor, String> {

}
