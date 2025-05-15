package com.reactive.programming.challenge.domain.spi;

import com.reactive.programming.challenge.domain.model.Technology;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IWebClientPersistencePort {
    Mono<Technology> getTechnology(Long id);
    Mono<Void> deleteTechnology(Long id);
    Flux<Technology> findByIdIn(List<Long> ids);
}
