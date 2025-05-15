package com.reactive.programming.challenge.infrastructure.adapter;

import com.reactive.programming.challenge.domain.model.Technology;
import com.reactive.programming.challenge.domain.spi.IWebClientPersistencePort;
import com.reactive.programming.challenge.infrastructure.entrypoints.TechnologyClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class WebClientAdapter implements IWebClientPersistencePort {
    private final TechnologyClient technologyClient;

    public WebClientAdapter(TechnologyClient technologyClient) {
        this.technologyClient = technologyClient;
    }

    @Override
    public Mono<Technology> getTechnology(Long id) {
        return technologyClient.getTechnology(id);
    }

    @Override
    public Mono<Void> deleteTechnology(Long id) {
        return technologyClient.deleteTechnology(id);
    }

    @Override
    public Flux<Technology> findByIdIn(List<Long> ids) {
        return technologyClient.findByIdIn(ids);
    }
}
