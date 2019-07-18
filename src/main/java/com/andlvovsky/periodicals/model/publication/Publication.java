package com.andlvovsky.periodicals.model.publication;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class Publication {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
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
