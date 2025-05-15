package com.reactive.programming.challenge.infrastructure.entrypoints.mapper;

import com.reactive.programming.challenge.domain.model.Capacity;
import com.reactive.programming.challenge.infrastructure.entrypoints.dto.CapacityDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-14T22:17:16-0500",
    comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.13.jar, environment: Java 17.0.15 (Amazon.com Inc.)"
)
@Component
public class CapacityMapperImpl implements CapacityMapper {

    @Override
    public Capacity capacityDTOToCapacity(CapacityDTO capacityDTO) {
        if ( capacityDTO == null ) {
            return null;
        }

        Capacity capacity = new Capacity();

        capacity.setName( capacityDTO.getName() );
        capacity.setDescription( capacityDTO.getDescription() );
        List<Long> list = capacityDTO.getTechnologies();
        if ( list != null ) {
            capacity.setTechnologies( new ArrayList<Long>( list ) );
        }

        return capacity;
    }
}
