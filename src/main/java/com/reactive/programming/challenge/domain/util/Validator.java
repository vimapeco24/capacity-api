package com.reactive.programming.challenge.domain.util;

import com.reactive.programming.challenge.domain.enums.TechnicalMessage;
import com.reactive.programming.challenge.domain.exceptions.BusinessException;
import com.reactive.programming.challenge.domain.model.Capacity;
import com.reactive.programming.challenge.domain.spi.IWebClientPersistencePort;
import com.reactive.programming.challenge.domain.usecase.CapacityUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;


public class Validator {
    private static final Logger log = LoggerFactory.getLogger(Validator.class);
    private final IWebClientPersistencePort webClientPersistencePort;

    public Validator(IWebClientPersistencePort webClientPersistencePort) {
        this.webClientPersistencePort = webClientPersistencePort;
    }

    public Mono<Void> validateCapacity(Capacity capacity) {
        if (capacity.getName() == null || capacity.getName().length() > Constants.NAME_CHARACTER_LIMIT) {
            return Mono.error(new BusinessException(TechnicalMessage.NAME_CHARACTER_LIMIT));
        }
        if (capacity.getDescription() == null || capacity.getDescription().length() > Constants.DESCRIPTION_CHARACTER_LIMIT) {
            return Mono.error(new BusinessException(TechnicalMessage.DESCRIPTION_CHARACTER_LIMIT));
        }
        if (capacity.getName().matches("^\\d+$")) {
            return Mono.error(new BusinessException(TechnicalMessage.NOT_ONLY_NUMBERS));
        }
        return Mono.empty();
    }

    public Mono<Void> validateTechnologies(List<Long> technologies) {
        if (technologies == null || technologies.size() < 3) {
            return Mono.error(new BusinessException(TechnicalMessage.MIN_NUMBER_TECHNOLOGIES));
        }
        if (technologies.size() > 20) {
            return Mono.error(new BusinessException(TechnicalMessage.MAX_NUMBER_TECHNOLOGIES));
        }
        if (hasDuplicates(technologies)) {
            return Mono.error(new BusinessException(TechnicalMessage.DUPLICATED_TECHNOLOGIES));
        }
        return Flux.fromIterable(technologies)
                .flatMap(id -> webClientPersistencePort.getTechnology(id)
                        .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.TECHNOLOGY_NOT_FOUND)))
                        .doOnNext(tech -> log.info("Tecnología encontrada {}", tech.getName()))
                        .onErrorMap(ex -> {
                            log.error("Error al buscar tecnología con ID {}: {}", id, ex.getMessage());
                            return new BusinessException(TechnicalMessage.TECHNOLOGY_NOT_FOUND);
                        })
                )
                .then();
    }

    private boolean hasDuplicates(List<Long> list) {
        return list.size() != new HashSet<>(list).size();
    }
}