package com.reactive.programming.challenge.domain.spi;

import com.reactive.programming.challenge.domain.model.CapacityTechnology;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ICapacityTechnologyPersistencePort {
    Flux<CapacityTechnology> createAll(List<CapacityTechnology> relations);
    Flux<CapacityTechnology> findAllByCapacityId(Long capacityId);
    Mono<Long> countCapacitiesByTechnologyId(Long technologyId);
    Mono<Void> deleteByCapacityId(Long capacityId);
}
