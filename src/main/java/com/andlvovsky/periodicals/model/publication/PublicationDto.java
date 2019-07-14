package com.andlvovsky.periodicals.model.publication;

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

    @NotNull
    @Size(min=2)
    private String name;

    @NotNull
    @Min(1)
    private Integer frequency; /** days between publications */

    @NotNull
    @DecimalMin("0.0")
    private Double cost; /** cost per publication */

    private String description;

}
