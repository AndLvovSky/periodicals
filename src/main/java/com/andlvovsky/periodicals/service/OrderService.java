package com.andlvovsky.periodicals.service;

import com.andlvovsky.periodicals.dto.Money;
import com.andlvovsky.periodicals.model.basket.Basket;

public interface OrderService {

    Money calculateCost(Basket basket);

    void registerOrder(Basket basket);

}
