package com.reactive.programming.challenge.domain.spi;

import com.reactive.programming.challenge.domain.model.Capacity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ICapacityPersistencePort {
    Mono<Capacity> createCapacity(Capacity capacity);
    Flux<Capacity> getCapacities();
    Mono<Capacity> getCapacityById(Long id);
    Mono<Void> deleteCapacity(Long id);
    Flux<Capacity> findByIdIn(List<Long> ids);
    Mono<Boolean> existsByName(String name);
}
