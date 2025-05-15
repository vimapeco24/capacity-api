package com.reactive.programming.challenge.application.config;

import com.reactive.programming.challenge.domain.api.ICapacityServicePort;
import com.reactive.programming.challenge.domain.api.ICapacityTechnologyServicePort;
import com.reactive.programming.challenge.domain.api.IWebClientServicePort;
import com.reactive.programming.challenge.domain.spi.ICapacityPersistencePort;
import com.reactive.programming.challenge.domain.spi.ICapacityTechnologyPersistencePort;
import com.reactive.programming.challenge.domain.spi.IWebClientPersistencePort;
import com.reactive.programming.challenge.domain.usecase.CapacityTechnologyUseCase;
import com.reactive.programming.challenge.domain.usecase.CapacityUseCase;
import com.reactive.programming.challenge.domain.usecase.WebClientUseCase;
import com.reactive.programming.challenge.infrastructure.entrypoints.TechnologyClient;
import com.reactive.programming.challenge.infrastructure.adapter.CapacityAdapter;
import com.reactive.programming.challenge.infrastructure.adapter.CapacityTechnologyAdapter;
import com.reactive.programming.challenge.infrastructure.adapter.WebClientAdapter;
import com.reactive.programming.challenge.infrastructure.adapter.mapper.CapacityEntityMapper;
import com.reactive.programming.challenge.infrastructure.adapter.mapper.CapacityTechnologyEntityMapper;
import com.reactive.programming.challenge.infrastructure.adapter.repository.CapacityRepository;
import com.reactive.programming.challenge.infrastructure.adapter.repository.CapacityTechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {
    private final CapacityRepository capacityRepository;
    private final CapacityTechnologyRepository capacityTechnologyRepository;
    private final CapacityEntityMapper capacityEntityMapper;
    private final CapacityTechnologyEntityMapper capacityTechnologyEntityMapper;
    private final TechnologyClient technologyClient;

    @Bean
    public ICapacityPersistencePort capacityPersistencePort() {
        return new CapacityAdapter(capacityRepository, capacityEntityMapper);
    }

    @Bean
    public ICapacityTechnologyPersistencePort capacityTechnologyPersistencePort() {
        return new CapacityTechnologyAdapter(capacityTechnologyRepository, capacityTechnologyEntityMapper);
    }

    @Bean
    public IWebClientPersistencePort webClientPersistencePort() {
        return new WebClientAdapter(technologyClient);
    }

    @Bean
    public ICapacityServicePort capacityServicePort(ICapacityPersistencePort capacityPersistencePort, IWebClientPersistencePort webClientPersistencePort, ICapacityTechnologyPersistencePort capacityTechnologyPersistencePort) {
        return new CapacityUseCase(capacityPersistencePort, webClientPersistencePort, capacityTechnologyPersistencePort);
    }

    @Bean
    public ICapacityTechnologyServicePort capacityTechnologyServicePort(ICapacityTechnologyPersistencePort capacityTechnologyPersistencePort) {
        return new CapacityTechnologyUseCase(capacityTechnologyPersistencePort);
    }

    @Bean
    public IWebClientServicePort webClientServicePort(IWebClientPersistencePort webClientPersistencePort) {
        return new WebClientUseCase(webClientPersistencePort);
    }
}
