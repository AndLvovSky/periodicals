package com.andlvovsky.periodicals.model.publication;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PublicationDto {

    private Long id;

    private String name;

    private Integer frequency; /** days between publications */

    private Double cost; /** cost per publication */

    private String description;

}
