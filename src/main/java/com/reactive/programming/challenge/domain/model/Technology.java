package com.reactive.programming.challenge.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Technology {
    private Long id;
    private String name;
    private String description;

    public Technology(String name, String description) {
        this.name = name;
        this.description = description;
    }
}