package com.andlvovsky.periodicals.model.basket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketDto {

    private List<BasketItemDto> items;

}
