package com.andlvovsky.periodicals.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "publication_generator")
    @SequenceGenerator(name = "publication_generator", sequenceName = "publication_sequence", allocationSize = 1)
    private Long id;

    private String name;

    private Integer period;

    private Double cost;

    private String description;

    public Publication(String name, Integer period, Double cost, String description) {
        this.name = name;
        this.period = period;
        this.cost = cost;
        this.description = description;
    }

}
