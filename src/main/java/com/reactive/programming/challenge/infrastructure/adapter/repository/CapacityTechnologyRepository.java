package com.reactive.programming.challenge.infrastructure.adapter.repository;

import com.reactive.programming.challenge.infrastructure.adapter.entity.CapacityTechnologyEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CapacityTechnologyRepository extends ReactiveCrudRepository<CapacityTechnologyEntity, Long> {
    Flux<CapacityTechnologyEntity> findAllByCapacityId(Long capacityId);

    Mono<Void> deleteByCapacityId(Long capacityId);

    Mono<Long> countByTechnologyId(Long technologyId);
}
