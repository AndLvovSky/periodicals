package com.andlvovsky.periodicals.model.basket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketDto {

    private Long publicationId;

    private Integer number;

}
