package com.reactive.programming.challenge.domain.usecase;

import com.reactive.programming.challenge.domain.api.IWebClientServicePort;
import com.reactive.programming.challenge.domain.model.Technology;
import com.reactive.programming.challenge.domain.spi.IWebClientPersistencePort;
import reactor.core.publisher.Flux;

import java.util.List;

public class WebClientUseCase implements IWebClientServicePort {
    private final IWebClientPersistencePort webClientPersistencePort;

    public WebClientUseCase(IWebClientPersistencePort webClientPersistencePort) {
        this.webClientPersistencePort = webClientPersistencePort;
    }

    @Override
    public Flux<Technology> findByIdIn(List<Long> ids) {
        return webClientPersistencePort.findByIdIn(ids);
    }
}
