package com.reactive.programming.challenge.infrastructure.entrypoints;

import com.reactive.programming.challenge.domain.model.Technology;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class TechnologyClient {
    private final WebClient webClient;

    public TechnologyClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8080").build();
    }

    public Mono<Technology> getTechnology(Long id) {
        return webClient.get()
                .uri("/technologies/api/technology/{id}", id)
                .retrieve()
                .bodyToMono(Technology.class);
    }

    public Mono<Void> deleteTechnology(Long id) {
        return webClient.delete()
                .uri("/technologies/api/technology/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Flux<Technology> findByIdIn(List<Long> ids) {
        return webClient.post()
                .uri("/technologies/api/technologiesByIdIn")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ids)
                .retrieve()
                .bodyToFlux(Technology.class);
    }
}
