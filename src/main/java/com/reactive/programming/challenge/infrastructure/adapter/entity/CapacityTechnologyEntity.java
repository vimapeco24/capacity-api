package com.reactive.programming.challenge.infrastructure.adapter.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@Table(name = "capability_technologies")
@Getter
@Setter
@RequiredArgsConstructor
public class CapacityTechnologyEntity {

    @Column("capability_id")
    private Long capacityId;

    @Column("technology_id")
    private Long technologyId;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
