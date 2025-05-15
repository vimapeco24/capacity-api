package com.reactive.programming.challenge.infrastructure.adapter;

import com.reactive.programming.challenge.domain.model.Capacity;
import com.reactive.programming.challenge.domain.spi.ICapacityPersistencePort;
import com.reactive.programming.challenge.infrastructure.adapter.mapper.CapacityEntityMapper;
import com.reactive.programming.challenge.infrastructure.adapter.repository.CapacityRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class CapacityAdapter implements ICapacityPersistencePort {
    private final CapacityRepository capacityRepository;
    private final CapacityEntityMapper capacityEntityMapper;

    @Override
    public Mono<Capacity> createCapacity(Capacity capacity) {
        return capacityRepository.save(capacityEntityMapper.toEntity(capacity))
                .map(capacityEntityMapper::toCapacity);
    }

    @Override
    public Flux<Capacity> getCapacities() {
        return capacityRepository.findAll()
                .map(capacityEntityMapper::toCapacity);
    }

    @Override
    public Mono<Capacity> getCapacityById(Long id) {
        return capacityRepository.findById(id)
                .map(capacityEntityMapper::toCapacity);
    }

    @Override
    public Mono<Void> deleteCapacity(Long id) {
        return capacityRepository.deleteById(id);
    }

    @Override
    public Flux<Capacity> findByIdIn(List<Long> ids) {
        return capacityRepository.findByIdIn(ids)
                .map(capacityEntityMapper::toCapacity);
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        log.info("Checking if capacity exists with name {}", name);
        return capacityRepository.findByName(name)
                .map(capacityEntityMapper::toCapacity)
                .map(capacityEntity -> true)
                .defaultIfEmpty(false);
    }
}
