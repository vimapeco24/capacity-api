package com.reactive.programming.challenge.domain.api;

import com.reactive.programming.challenge.domain.model.Capacity;
import com.reactive.programming.challenge.domain.model.CapacityTechnology;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ICapacityServicePort {
    Mono<Capacity> registerCapacity(Capacity capacity);
    Flux<Capacity> getCapacities();
    Mono<Capacity> getCapacityById(Long id);
    Mono<Void> deleteCapacity(Long id);
    Flux<Capacity> getCapacitiesByIdIn(List<Long> ids);
    Flux<CapacityTechnology> getCapacityTechnologyByCapacityId(Long capacityId);
}
