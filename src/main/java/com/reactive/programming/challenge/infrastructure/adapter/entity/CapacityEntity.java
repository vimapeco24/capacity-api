package com.reactive.programming.challenge.infrastructure.adapter.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "capabilities")
@Getter
@Setter
@RequiredArgsConstructor
public class CapacityEntity {
    @Id
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String description;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
