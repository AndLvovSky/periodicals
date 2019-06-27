package com.andlvovsky.periodicals.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Publication {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Integer frequency; /** days between publications */

    private Float cost; /** cost per publication */

    private String description;

    public Publication() {}

    public Publication(String name, Integer frequency, Float cost, String description) {
        this.name = name;
        this.frequency = frequency;
        this.cost = cost;
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public Float getCost() {
        return cost;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publication that = (Publication) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(frequency, that.frequency) &&
                Objects.equals(cost, that.cost) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, frequency, cost, description);
    }

    @Override
    public String toString() {
        return "Publication{" +
                "name='" + name + '\'' +
                ", frequency=" + frequency +
                ", cost=" + cost +
                ", description='" + description + '\'' +
                '}';
    }

}
