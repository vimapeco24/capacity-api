package com.reactive.programming.challenge.infrastructure.adapter.repository;

import com.reactive.programming.challenge.infrastructure.adapter.entity.CapacityEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

public interface CapacityRepository extends ReactiveCrudRepository<CapacityEntity, Long> {
    Mono<CapacityEntity> findByName(String name);

    Flux<CapacityEntity> findByIdIn(Collection<Long> ids);
}
