package com.andlvovsky.periodicals.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicationDto {

    private Long id;

    @NotNull
    @Size(min = 2, max = 1000)
    private String name;

    @NotNull
    @Min(1)
    private Integer period; /** days between publishing */

    @NotNull
    @DecimalMin("0.0")
    private Double cost; /** cost per publication in dollars */

    private String description;

    public PublicationDto(String name, Integer period, Double cost, String description) {
        this.name = name;
        this.period = period;
        this.cost = cost;
        this.description = description;
    }

}
