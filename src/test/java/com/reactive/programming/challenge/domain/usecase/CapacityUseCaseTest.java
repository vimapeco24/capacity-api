package com.reactive.programming.challenge.domain.usecase;

import com.reactive.programming.challenge.domain.enums.TechnicalMessage;
import com.reactive.programming.challenge.domain.exceptions.BusinessException;
import com.reactive.programming.challenge.domain.model.Capacity;
import com.reactive.programming.challenge.domain.model.CapacityTechnology;
import com.reactive.programming.challenge.domain.spi.ICapacityPersistencePort;
import com.reactive.programming.challenge.domain.spi.ICapacityTechnologyPersistencePort;
import com.reactive.programming.challenge.domain.spi.IWebClientPersistencePort;
import com.reactive.programming.challenge.domain.util.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CapacityUseCaseTest {

    @Mock
    private ICapacityPersistencePort capacityPersistencePort;

    @Mock
    private ICapacityTechnologyPersistencePort capacityTechnologyPersistencePort;

    @Mock
    private IWebClientPersistencePort webClientPersistencePort;

    @Mock
    private Validator validator;

    @InjectMocks
    private CapacityUseCase capacityUseCase;

    private Capacity validCapacity;
    private CapacityTechnology validCapacityTechnology;

    @BeforeEach
    void setUp() {
        validCapacity = new Capacity(1L, "Valid Capacity", "Valid Description", List.of(1L, 2L));
        validCapacityTechnology = new CapacityTechnology(1L, 1L, 1L);
    }

    // Tests para registerCapacity
    @Test
    void registerCapacity_WhenNull_ShouldThrowBusinessException() {
        StepVerifier.create(capacityUseCase.registerCapacity(null))
                .expectErrorMatches(ex -> isBusinessExceptionWithMessage(ex, TechnicalMessage.INVALID_CAPACITY_DATA))
                .verify();

        verifyNoInteractions(validator, capacityPersistencePort, capacityTechnologyPersistencePort);
    }

    @Test
    void registerCapacity_WhenEmptyName_ShouldThrowBusinessException() {
        Capacity invalidCapacity = new Capacity(null, "", "Description", List.of(1L));

        StepVerifier.create(capacityUseCase.registerCapacity(invalidCapacity))
                .expectErrorMatches(ex -> isBusinessExceptionWithMessage(ex, TechnicalMessage.INVALID_CAPACITY_DATA))
                .verify();

        verifyNoInteractions(validator, capacityPersistencePort, capacityTechnologyPersistencePort);
    }

    @Test
    void registerCapacity_WhenEmptyTechnologies_ShouldThrowBusinessException() {
        Capacity invalidCapacity = new Capacity(null, "Name", "Description", Collections.emptyList());

        StepVerifier.create(capacityUseCase.registerCapacity(invalidCapacity))
                .expectErrorMatches(ex -> isBusinessExceptionWithMessage(ex, TechnicalMessage.MIN_NUMBER_TECHNOLOGIES))
                .verify();

        verifyNoInteractions(validator, capacityPersistencePort, capacityTechnologyPersistencePort);
    }

    @Test
    void registerCapacity_WhenValidationFails_ShouldThrowBusinessException() {
        when(validator.validateCapacity(any())).thenReturn(Mono.error(new BusinessException(TechnicalMessage.VALIDATION_FAILED)));
        when(validator.validateTechnologies(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(capacityUseCase.registerCapacity(validCapacity))
                .expectErrorMatches(ex -> isBusinessExceptionWithMessage(ex, TechnicalMessage.VALIDATION_FAILED))
                .verify();
    }

    @Test
    void registerCapacity_WhenNameExists_ShouldThrowBusinessException() {
        when(validator.validateCapacity(any())).thenReturn(Mono.empty());
        when(validator.validateTechnologies(anyList())).thenReturn(Mono.empty());
        when(capacityPersistencePort.existsByName(anyString())).thenReturn(Mono.just(true));

        StepVerifier.create(capacityUseCase.registerCapacity(validCapacity))
                .expectErrorMatches(ex -> isBusinessExceptionWithMessage(ex, TechnicalMessage.CAPACITY_ALREADY_EXISTS))
                .verify();
    }

    @Test
    void registerCapacity_WhenValid_ShouldReturnSavedCapacity() {
        when(validator.validateCapacity(any())).thenReturn(Mono.empty());
        when(validator.validateTechnologies(anyList())).thenReturn(Mono.empty());
        when(capacityPersistencePort.existsByName(anyString())).thenReturn(Mono.just(false));
        when(capacityPersistencePort.createCapacity(any())).thenReturn(Mono.just(validCapacity));
        when(capacityTechnologyPersistencePort.createAll(anyList()))
                .thenReturn(Flux.just(validCapacityTechnology, validCapacityTechnology));

        StepVerifier.create(capacityUseCase.registerCapacity(validCapacity))
                .expectNextMatches(c -> c.getId().equals(1L) && c.getName().equals("Valid Capacity"))
                .verifyComplete();
    }

    // Resto de los tests permanecen igual...
    // ... [otros métodos de prueba sin cambios]

    private boolean isBusinessExceptionWithMessage(Throwable ex, TechnicalMessage expectedMessage) {
        return ex instanceof BusinessException &&
                ((BusinessException) ex).getTechnicalMessage() == expectedMessage;
    }
}