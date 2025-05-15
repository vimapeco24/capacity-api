package com.reactive.programming.challenge.infrastructure.adapter.mapper;

import com.reactive.programming.challenge.domain.model.CapacityTechnology;
import com.reactive.programming.challenge.infrastructure.adapter.entity.CapacityTechnologyEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-14T22:17:16-0500",
    comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.13.jar, environment: Java 17.0.15 (Amazon.com Inc.)"
)
@Component
public class CapacityTechnologyEntityMapperImpl implements CapacityTechnologyEntityMapper {

    @Override
    public CapacityTechnology toCapacityTechnology(CapacityTechnologyEntity capacityTechnologyEntity) {
        if ( capacityTechnologyEntity == null ) {
            return null;
        }

        CapacityTechnology capacityTechnology = new CapacityTechnology();

        capacityTechnology.setCapacityId( capacityTechnologyEntity.getCapacityId() );
        capacityTechnology.setTechnologyId( capacityTechnologyEntity.getTechnologyId() );

        return capacityTechnology;
    }

    @Override
    public CapacityTechnologyEntity toEntity(CapacityTechnology capacityTechnology) {
        if ( capacityTechnology == null ) {
            return null;
        }

        CapacityTechnologyEntity capacityTechnologyEntity = new CapacityTechnologyEntity();

        capacityTechnologyEntity.setCapacityId( capacityTechnology.getCapacityId() );
        capacityTechnologyEntity.setTechnologyId( capacityTechnology.getTechnologyId() );

        return capacityTechnologyEntity;
    }
}
