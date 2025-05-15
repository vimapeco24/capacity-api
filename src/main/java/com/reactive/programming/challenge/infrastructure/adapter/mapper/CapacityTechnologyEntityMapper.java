package com.reactive.programming.challenge.infrastructure.adapter.mapper;

import com.reactive.programming.challenge.domain.model.CapacityTechnology;
import com.reactive.programming.challenge.infrastructure.adapter.entity.CapacityTechnologyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CapacityTechnologyEntityMapper {


    @Mapping(target = "id", ignore = true)
    CapacityTechnology toCapacityTechnology(CapacityTechnologyEntity capacityTechnologyEntity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CapacityTechnologyEntity toEntity(CapacityTechnology capacityTechnology);
}
