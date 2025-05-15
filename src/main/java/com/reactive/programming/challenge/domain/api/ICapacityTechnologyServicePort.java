package com.reactive.programming.challenge.domain.api;

import com.reactive.programming.challenge.domain.model.CapacityTechnology;
import reactor.core.publisher.Flux;

public interface ICapacityTechnologyServicePort {
    Flux<CapacityTechnology> findAllByCapacityId(Long capacityId);
}
