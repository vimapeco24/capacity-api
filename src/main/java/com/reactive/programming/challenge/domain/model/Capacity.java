package com.reactive.programming.challenge.domain.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Capacity {
    private Long id;
    private String name;
    private String description;
    private List<Long> technologies;

    public Capacity(String name, String description, List<Long> technologies) {
        this.name = name;
        this.description = description;
        this.technologies = technologies;
    }

    public Capacity(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
