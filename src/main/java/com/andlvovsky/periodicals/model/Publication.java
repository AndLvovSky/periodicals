package com.andlvovsky.periodicals.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Publication {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Integer frequency; /** days between publications */

    private Double cost; /** cost per publication */

    private String description;

    public Publication() {}

    public Publication(String name, Integer frequency, Double cost, String description) {
        this.name = name;
        this.frequency = frequency;
        this.cost = cost;
        this.description = description;
    }

}
