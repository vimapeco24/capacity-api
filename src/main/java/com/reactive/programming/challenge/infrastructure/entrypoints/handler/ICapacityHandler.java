package com.reactive.programming.challenge.infrastructure.entrypoints.handler;

import com.reactive.programming.challenge.domain.enums.TechnicalMessage;
import com.reactive.programming.challenge.infrastructure.entrypoints.util.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ICapacityHandler {
    Mono<ServerResponse> createCapacity(ServerRequest request);
    Mono<ServerResponse> getCapacities(ServerRequest request);
    Mono<ServerResponse> getCapacitiesByBootcampId(ServerRequest request);
    Mono<ServerResponse> getCapacitiesWithTechnologiesByBootcampId(ServerRequest request);
    Mono<ServerResponse> getCapacityById(ServerRequest request);
    Mono<ServerResponse> deleteCapacity(ServerRequest request);
    Mono<ServerResponse> getCapacitiesByIdIn(ServerRequest request);
    Mono<ServerResponse> buildErrorResponse(HttpStatus httpStatus, String identifier, TechnicalMessage error, List<ErrorDTO> errors);
}
