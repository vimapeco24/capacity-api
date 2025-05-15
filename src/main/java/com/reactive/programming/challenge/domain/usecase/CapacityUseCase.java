package com.reactive.programming.challenge.domain.usecase;

import com.reactive.programming.challenge.domain.api.ICapacityServicePort;
import com.reactive.programming.challenge.domain.enums.TechnicalMessage;
import com.reactive.programming.challenge.domain.exceptions.BusinessException;
import com.reactive.programming.challenge.domain.model.Capacity;
import com.reactive.programming.challenge.domain.model.CapacityTechnology;
import com.reactive.programming.challenge.domain.spi.ICapacityPersistencePort;
import com.reactive.programming.challenge.domain.spi.ICapacityTechnologyPersistencePort;
import com.reactive.programming.challenge.domain.spi.IWebClientPersistencePort;
import com.reactive.programming.challenge.domain.util.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class CapacityUseCase implements ICapacityServicePort {

    private static final Logger log = LoggerFactory.getLogger(CapacityUseCase.class);

    private final ICapacityPersistencePort capacityPersistencePort;
    private final ICapacityTechnologyPersistencePort capacityTechnologyPersistencePort;
    private final IWebClientPersistencePort webClientPersistencePort;
    private final Validator validator;

    public CapacityUseCase(ICapacityPersistencePort capacityPersistencePort,
                           IWebClientPersistencePort webClientPersistencePort,
                           ICapacityTechnologyPersistencePort capacityTechnologyPersistencePort) {
        this.capacityPersistencePort = capacityPersistencePort;
        this.capacityTechnologyPersistencePort = capacityTechnologyPersistencePort;
        this.webClientPersistencePort = webClientPersistencePort;
        this.validator = new Validator(webClientPersistencePort);
    }

    @Override
    public Mono<Capacity> registerCapacity(Capacity capacity) {
        log.info("Registering capacity: {}", capacity != null ? capacity.getName() : "null");

        // Validaciones iniciales
        if (capacity == null || capacity.getName() == null || capacity.getName().isEmpty()) {
            return Mono.error(new BusinessException(TechnicalMessage.INVALID_CAPACITY_DATA));
        }

        List<Long> techIds = capacity.getTechnologies();
        if (techIds == null || techIds.isEmpty()) {
            return Mono.error(new BusinessException(TechnicalMessage.MIN_NUMBER_TECHNOLOGIES));
        }

        return Mono.just(capacity)
                // Validación que mantiene el objeto Capacity
                .flatMap(c -> validateCapacity(c).thenReturn(c))
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.VALIDATION_FAILED)))
                // Verificación de nombre existente
                .flatMap(validatedCapacity ->
                        capacityPersistencePort.existsByName(validatedCapacity.getName())
                                .flatMap(exists -> {
                                    if (exists) {
                                        return Mono.error(new BusinessException(TechnicalMessage.CAPACITY_ALREADY_EXISTS));
                                    }
                                    return Mono.just(validatedCapacity);
                                }))
                // Creación de la capacidad
                .flatMap(validatedCapacity ->
                        capacityPersistencePort.createCapacity(validatedCapacity)
                                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.CAPACITY_CREATION_FAILED))))
                // Creación de relaciones con tecnologías
                .flatMap(savedCapacity -> {
                    List<CapacityTechnology> relations = techIds.stream()
                            .map(techId -> new CapacityTechnology(null, savedCapacity.getId(), techId))
                            .toList();

                    return capacityTechnologyPersistencePort.createAll(relations)
                            .collectList()
                            .flatMap(results -> {
                                if (results.isEmpty()) {
                                    return Mono.error(new BusinessException(
                                            TechnicalMessage.CAPACITY_TECHNOLOGY_CREATION_FAILED));
                                }
                                return Mono.just(savedCapacity);
                            });
                })
                // Manejo de errores
                .onErrorResume(e -> {
                    if (e instanceof BusinessException) {
                        return Mono.error(e);
                    }
                    log.error("Unknown error registering capacity", e);
                    return Mono.error(new BusinessException(TechnicalMessage.UNKNOWN_ERROR));
                });
    }

    private Mono<Void> validateCapacity(Capacity capacity) {
        return Mono.defer(() -> {
                    validator.validateCapacity(capacity);
                    return Mono.empty();
                })
                .then(Mono.defer(() -> {
                    validator.validateTechnologies(capacity.getTechnologies());
                    return Mono.empty();
                }));
    }

    @Override
    public Flux<Capacity> getCapacities() {
        return capacityPersistencePort.getCapacities();
    }

    @Override
    public Mono<Capacity> getCapacityById(Long id) {
        return capacityPersistencePort.getCapacityById(id);
    }

    @Override
    public Mono<Void> deleteCapacity(Long id) {
        return capacityTechnologyPersistencePort.deleteByCapacityId(id)
                .then(capacityPersistencePort.deleteCapacity(id))
                .then(handleTechnologyCleanup(id));
    }

    private Mono<Void> handleTechnologyCleanup(Long capacityId) {
        return getCapacityById(capacityId)
                .flatMap(capacity -> {
                    List<Long> techIds = capacity.getTechnologies();
                    return Mono.just(techIds)
                            .thenMany(Flux.fromIterable(techIds))
                            .flatMap(techId ->
                                    capacityTechnologyPersistencePort.countCapacitiesByTechnologyId(techId)
                                            .flatMap(tCount -> {
                                                log.info("Technology {} count: {}", techId, tCount);
                                                if (tCount == 1) {
                                                    return webClientPersistencePort.deleteTechnology(techId)
                                                            .doOnTerminate(() -> log.info("Technology {} deleted", techId));
                                                }
                                                return Mono.empty();
                                            })
                            ).then();
                });
    }

    @Override
    public Flux<Capacity> getCapacitiesByIdIn(List<Long> ids) {
        log.info("Fetching capacities with ids: {}", ids);
        return capacityPersistencePort.findByIdIn(ids);
    }

    @Override
    public Flux<CapacityTechnology> getCapacityTechnologyByCapacityId(Long capacityId) {
        return capacityTechnologyPersistencePort.findAllByCapacityId(capacityId);
    }
}
