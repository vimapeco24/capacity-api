package com.reactive.programming.challenge.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class CapacityTechnology {
    private Long id;
    private Long capacityId;
    private Long technologyId;

    public CapacityTechnology(Long id, Long capacityId, Long technologyId) {
        this.id = id;
        this.capacityId = capacityId;
        this.technologyId = technologyId;
    }
}
