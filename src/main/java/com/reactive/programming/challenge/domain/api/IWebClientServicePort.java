package com.reactive.programming.challenge.domain.api;

import com.reactive.programming.challenge.domain.model.Technology;
import reactor.core.publisher.Flux;

import java.util.List;

public interface IWebClientServicePort {
    Flux<Technology> findByIdIn(List<Long> ids);
}
