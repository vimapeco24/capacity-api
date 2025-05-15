package com.reactive.programming.challenge.infrastructure.adapter.mapper;

import com.reactive.programming.challenge.domain.model.Capacity;
import com.reactive.programming.challenge.infrastructure.adapter.entity.CapacityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CapacityEntityMapper {

    @Mapping(target = "technologies", ignore = true)
    Capacity toCapacity(CapacityEntity capacityEntity);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CapacityEntity toEntity(Capacity capacity);
}
