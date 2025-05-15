package com.reactive.programming.challenge.infrastructure.adapter.mapper;

import com.reactive.programming.challenge.domain.model.Capacity;
import com.reactive.programming.challenge.infrastructure.adapter.entity.CapacityEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-14T22:17:16-0500",
    comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.13.jar, environment: Java 17.0.15 (Amazon.com Inc.)"
)
@Component
public class CapacityEntityMapperImpl implements CapacityEntityMapper {

    @Override
    public Capacity toCapacity(CapacityEntity capacityEntity) {
        if ( capacityEntity == null ) {
            return null;
        }

        Capacity capacity = new Capacity();

        capacity.setId( capacityEntity.getId() );
        capacity.setName( capacityEntity.getName() );
        capacity.setDescription( capacityEntity.getDescription() );

        return capacity;
    }

    @Override
    public CapacityEntity toEntity(Capacity capacity) {
        if ( capacity == null ) {
            return null;
        }

        String name = null;
        String description = null;

        name = capacity.getName();
        description = capacity.getDescription();

        CapacityEntity capacityEntity = new CapacityEntity( name, description );

        capacityEntity.setId( capacity.getId() );

        return capacityEntity;
    }
}
