package com.reactive.programming.challenge.infrastructure.adapter;

import com.reactive.programming.challenge.domain.model.CapacityTechnology;
import com.reactive.programming.challenge.domain.spi.ICapacityTechnologyPersistencePort;
import com.reactive.programming.challenge.infrastructure.adapter.mapper.CapacityTechnologyEntityMapper;
import com.reactive.programming.challenge.infrastructure.adapter.repository.CapacityTechnologyRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
public class CapacityTechnologyAdapter implements ICapacityTechnologyPersistencePort {
    private final CapacityTechnologyRepository capacityTechnologyRepository;
    private final CapacityTechnologyEntityMapper capacityTechnologyEntityMapper;
    @Override
    public Flux<CapacityTechnology> createAll(List<CapacityTechnology> relations) {
        return capacityTechnologyRepository.saveAll(relations.stream().map(capacityTechnologyEntityMapper::toEntity).toList())
                .map(capacityTechnologyEntityMapper::toCapacityTechnology);
    }

    @Override
    public Flux<CapacityTechnology> findAllByCapacityId(Long capacityId) {
        return capacityTechnologyRepository.findAllByCapacityId(capacityId)
                .map(capacityTechnologyEntityMapper::toCapacityTechnology);
    }

    @Override
    public Mono<Long> countCapacitiesByTechnologyId(Long technologyId) {
        return capacityTechnologyRepository.countByTechnologyId(technologyId);
    }

    @Override
    public Mono<Void> deleteByCapacityId(Long capacityId) {
        return capacityTechnologyRepository.deleteByCapacityId(capacityId);
    }
}
