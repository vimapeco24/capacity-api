package com.reactive.programming.challenge.infrastructure.entrypoints.mapper;

import com.reactive.programming.challenge.infrastructure.entrypoints.dto.CapacityDTO;
import com.reactive.programming.challenge.domain.model.Capacity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CapacityMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "technologies", target = "technologies")
    Capacity capacityDTOToCapacity(CapacityDTO capacityDTO);}
