package com.reactive.programming.challenge.domain.usecase;

import com.reactive.programming.challenge.domain.api.ICapacityTechnologyServicePort;
import com.reactive.programming.challenge.domain.model.CapacityTechnology;
import com.reactive.programming.challenge.domain.spi.ICapacityTechnologyPersistencePort;
import reactor.core.publisher.Flux;

public class CapacityTechnologyUseCase implements ICapacityTechnologyServicePort {
    private final ICapacityTechnologyPersistencePort capacityTechnologyPersistencePort;

    public CapacityTechnologyUseCase(ICapacityTechnologyPersistencePort capacityTechnologyPersistencePort) {
        this.capacityTechnologyPersistencePort = capacityTechnologyPersistencePort;
    }

    @Override
    public Flux<CapacityTechnology> findAllByCapacityId(Long capacityId) {
        return capacityTechnologyPersistencePort.findAllByCapacityId(capacityId);
    }
}
