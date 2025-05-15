package com.reactive.programming.challenge.infrastructure.entrypoints.handler.impl;

import com.reactive.programming.challenge.domain.api.ICapacityTechnologyServicePort;
import com.reactive.programming.challenge.domain.api.IWebClientServicePort;
import com.reactive.programming.challenge.infrastructure.entrypoints.dto.CapacityDTO;
import com.reactive.programming.challenge.infrastructure.entrypoints.dto.CapacityResponse;
import com.reactive.programming.challenge.infrastructure.entrypoints.dto.TechnologyMinimalDTO;
import com.reactive.programming.challenge.infrastructure.entrypoints.handler.ICapacityHandler;
import com.reactive.programming.challenge.infrastructure.entrypoints.mapper.CapacityMapper;
import com.reactive.programming.challenge.infrastructure.entrypoints.util.APIResponse;
import com.reactive.programming.challenge.infrastructure.entrypoints.util.Constants;
import com.reactive.programming.challenge.domain.api.ICapacityServicePort;
import com.reactive.programming.challenge.domain.enums.TechnicalMessage;
import com.reactive.programming.challenge.infrastructure.entrypoints.util.ErrorDTO;
import com.reactive.programming.challenge.domain.exceptions.BusinessException;
import com.reactive.programming.challenge.domain.exceptions.TechnicalException;
import com.reactive.programming.challenge.domain.model.Capacity;
import com.reactive.programming.challenge.domain.model.CapacityTechnology;
import com.reactive.programming.challenge.domain.model.Technology;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.reactive.programming.challenge.infrastructure.entrypoints.util.Constants.CAPACITY_ERROR;
import static com.reactive.programming.challenge.infrastructure.entrypoints.util.Constants.X_MESSAGE_ID;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
@Slf4j
public class CapacityHandler implements ICapacityHandler {
    private final ICapacityServicePort capacityServicePort;
    private final CapacityMapper capacityMapper;
    private final IWebClientServicePort webClientServicePort;
    private final ICapacityTechnologyServicePort capacityTechnologyServicePort;

    @Override
    public Mono<ServerResponse> createCapacity(ServerRequest request) {
        String messageId = getMessageId(request);
        log.info(messageId);
        return request.bodyToMono(CapacityDTO.class)
                .flatMap(capacity -> capacityServicePort.registerCapacity(capacityMapper.capacityDTOToCapacity(capacity))
                        .doOnSuccess(capacitySaved -> log.info("{}: {}", Constants.CAPACITY_CREATED, messageId))
                )
                .flatMap(capacitySaved -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .bodyValue(TechnicalMessage.CAPACITY_CREATED.getMessage()))
                .contextWrite(Context.of(X_MESSAGE_ID, messageId))
                .doOnError(ex -> log.error(CAPACITY_ERROR, ex))
                .onErrorResume(BusinessException.class, ex -> buildErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        messageId,
                        TechnicalMessage.INVALID_PARAMETERS,
                        List.of(ErrorDTO.builder()
                                        .code(ex.getTechnicalMessage().getCode())
                                        .message(ex.getTechnicalMessage().getMessage())
                                        .param(ex.getTechnicalMessage().getParam())
                                .build())
                ))
                .onErrorResume(TechnicalException.class, ex -> buildErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        messageId,
                        TechnicalMessage.INTERNAL_ERROR,
                        List.of(ErrorDTO.builder()
                                .code(ex.getTechnicalMessage().getCode())
                                .message(ex.getTechnicalMessage().getMessage())
                                .param(ex.getTechnicalMessage().getParam())
                                .build())
                ))
                .onErrorResume(ex -> {
                    log.error("{}: {}", Constants.UNEXPECTED_ERROR, messageId, ex);
                    return buildErrorResponse(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            messageId,
                            TechnicalMessage.INTERNAL_ERROR,
                            List.of(ErrorDTO.builder()
                                            .code(TechnicalMessage.INTERNAL_ERROR.getCode())
                                            .message(TechnicalMessage.INTERNAL_ERROR.getMessage())
                                    .build())
                    );
                });
    }

    @Override
    public Mono<ServerResponse> getCapacities(ServerRequest request) {
        String sortBy = request.queryParam("sortBy").orElse("name");
        String direction = request.queryParam("direction").orElse("asc");
        int rawPage = Integer.parseInt(request.queryParam("page").orElse("0"));
        int rawSize = Integer.parseInt(request.queryParam("size").orElse("10"));

        final int page = Math.max(rawPage, 0);
        final int size = rawSize > 0 ? rawSize : 10;
        log.info(sortBy);
        log.info(direction);

        return capacityServicePort.getCapacities()
                .flatMap(cap ->
                        capacityTechnologyServicePort.findAllByCapacityId(cap.getId())
                                .map(CapacityTechnology::getTechnologyId)
                                .collectList()
                                .map(techIds -> {
                                    cap.setTechnologies(techIds); // Se actualiza la capacidad con sus techIds
                                    return cap;
                                })
                )
                .collectList()
                .flatMap(capacities -> {
                    log.info("Received capacity: {} - techs: {}", capacities.stream().map(Capacity::getName), capacities.stream().map(Capacity::getTechnologies));

                    List<Long> allTechIds = capacities.stream()
                            .flatMap(cap -> cap.getTechnologies().stream())
                            .distinct()
                            .toList();

                    return webClientServicePort.findByIdIn(allTechIds)
                            .collectList()
                            .map(technologies -> {
                                Map<Long, Technology> techmap = technologies.stream()
                                        .collect(Collectors.toMap(Technology::getId, Function.identity()));

                                // Mapear Capacity -> CapacityResponse
                                List<CapacityResponse> enrichedCapacities = capacities.stream()
                                        .map(cap -> {
                                            log.info("Capacity: {}, techIds: {}", cap.getName(), cap.getTechnologies());
                                            log.info("All requested tech IDs: {}", allTechIds);
                                            log.info("Technologies fetched: {}", technologies.stream().map(Technology::getId).toList());
                                        List<TechnologyMinimalDTO> techs = Optional.ofNullable(cap.getTechnologies())
                                                .orElse(List.of())
                                                .stream()
                                                .map(id -> {
                                                    Technology tech = techmap.get(id);
                                                    if (tech == null) {
                                                        log.warn("No technology found for ID: {}", id);
                                                    }
                                                    return new TechnologyMinimalDTO(tech.getId(), tech.getName());
                                                })
                                                .filter(Objects::nonNull)
                                                        .toList();
                                            return new CapacityResponse(cap.getId(), cap.getName(), cap.getDescription(), techs);
                                    })
                                        .toList();
                                List<CapacityResponse> sorted = sortCapacities(enrichedCapacities, sortBy, direction);
                                return sorted.stream()
                                        .skip((long) page * size)
                                        .limit(size)
                                        .toList();
                            });
                })
                .flatMap(sortedList -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Flux.fromIterable(sortedList), CapacityResponse.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getCapacitiesWithTechnologiesByBootcampId(ServerRequest request) {
        log.info("Flujo con capacidades según lista de ID's {}", Flux.just(capacityServicePort.getCapacitiesByIdIn(List.of(20L, 23L))));
        return request.bodyToMono(new ParameterizedTypeReference<List<Long>>() {})
                .flatMap(ids ->
                capacityServicePort.getCapacitiesByIdIn(ids)
                .flatMap(cap ->
                        capacityTechnologyServicePort.findAllByCapacityId(cap.getId())
                                .map(CapacityTechnology::getTechnologyId)
                                .collectList()
                                .map(techIds -> {
                                    //cap.setTechnologies(techIds); // Se actualiza la capacidad con sus techIds
                                    return new Capacity(cap.getId(), cap.getName(), cap.getDescription(), techIds);
                                })
                )
                .collectList()
                .flatMap(capacities -> {
                    log.info("Received capacity: {} - techs: {}", capacities.stream().map(Capacity::getName), capacities.stream().map(Capacity::getTechnologies));

                    List<Long> allTechIds = capacities.stream()
                            .flatMap(cap -> cap.getTechnologies().stream())
                            .distinct()
                            .toList();

                    return webClientServicePort.findByIdIn(allTechIds)
                            .collectList()
                            .map(technologies -> {
                                Map<Long, Technology> techmap = technologies.stream()
                                        .collect(Collectors.toMap(Technology::getId, Function.identity()));

                                // Mapear Capacity -> CapacityResponse
                                return capacities.stream()
                                        .map(cap -> {
                                            log.info("Capacity: {}, techIds: {}", cap.getName(), cap.getTechnologies());
                                            log.info("All requested tech IDs: {}", allTechIds);
                                            log.info("Technologies fetched: {}", technologies.stream().map(Technology::getId).toList());
                                            List<TechnologyMinimalDTO> techs = Optional.ofNullable(cap.getTechnologies())
                                                    .orElse(List.of())
                                                    .stream()
                                                    .map(id -> {
                                                        Technology tech = techmap.get(id);
                                                        if (tech == null) {
                                                            log.warn("No technology found for ID: {}", id);
                                                        }
                                                        return new TechnologyMinimalDTO(tech.getId(), tech.getName());
                                                    })
                                                    .filter(Objects::nonNull)
                                                    .toList();
                                            return new CapacityResponse(cap.getId(), cap.getName(), cap.getDescription(), techs);
                                        })
                                        .toList();
                            });
                })
                .flatMap(sortedList -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Flux.fromIterable(sortedList), CapacityResponse.class))
                .switchIfEmpty(ServerResponse.notFound().build()));
    }

    @Override
    public Mono<ServerResponse> getCapacitiesByBootcampId(ServerRequest request) {
        return request.bodyToMono(new ParameterizedTypeReference<List<Long>>() {})
                .flatMap(ids -> capacityServicePort.getCapacitiesByIdIn(ids)
                        .collectList()
                        .flatMap(capacities -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(capacities)
                        )
                );
    }

    @Override
    public Mono<ServerResponse> getCapacityById(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return capacityServicePort.getCapacityById(id)
                .flatMap(capacity -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(capacity)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @Override
    public Mono<ServerResponse> deleteCapacity(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        log.info("Deleting capacity with ID: {}", id);
        Mono<Void> capacityDeleted = capacityServicePort.deleteCapacity(id);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(capacityDeleted, Void.class);
    }

    @Override
    public Mono<ServerResponse> getCapacitiesByIdIn(ServerRequest request) {
        return request.bodyToMono(new ParameterizedTypeReference<List<Long>>() {})
                .flatMap(ids -> capacityServicePort.getCapacitiesByIdIn(ids)
                        .collectList()
                        .flatMap(capacities -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(capacities)
                        )
                );
    }



    private List<CapacityResponse> sortCapacities(List<CapacityResponse> capacities, String sortBy, String direction) {
        log.info(capacities.get(0).getTechnologies().toString());
        capacities.forEach(cap -> System.out.println(cap.getName() + " - tech count: " +
                (cap.getTechnologies() != null ? cap.getTechnologies().size() : 0)));

        Comparator<CapacityResponse> comparator;

        if ("name".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(CapacityResponse::getName, String.CASE_INSENSITIVE_ORDER);
        } else if ("technologyCount".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(cap ->
                    cap.getTechnologies() != null ? cap.getTechnologies().size() : 0);
        } else {
            comparator = Comparator.comparing(CapacityResponse::getId); // por defecto
        }

        if ("desc".equalsIgnoreCase(direction)) {
            comparator = comparator.reversed();
        }

        return capacities.stream()
                .sorted(comparator)
                .toList();
    }

    @Override
    public Mono<ServerResponse> buildErrorResponse(HttpStatus httpStatus, String identifier, TechnicalMessage error, List<ErrorDTO> errors) {
        return Mono.defer(() -> {
            APIResponse apiErrorResponse = APIResponse
                    .builder()
                    .code(error.getCode())
                    .message(error.getMessage())
                    .identifier(identifier)
                    .date(Instant.now().toString())
                    .errors(errors)
                    .build();
            return ServerResponse.status(httpStatus)
                    .bodyValue(apiErrorResponse);
        });
    }

    private String getMessageId(ServerRequest serverRequest) {
        return serverRequest.headers()
                .firstHeader(X_MESSAGE_ID) != null
                ? serverRequest.headers().firstHeader(X_MESSAGE_ID)
                : UUID.randomUUID().toString();
    }
}
