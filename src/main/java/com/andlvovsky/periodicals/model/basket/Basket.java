package com.andlvovsky.periodicals.model.basket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Basket {

    private List<BasketItem> items;

}
