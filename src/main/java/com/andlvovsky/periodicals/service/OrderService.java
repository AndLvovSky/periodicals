package com.andlvovsky.periodicals.service;

import com.andlvovsky.periodicals.model.basket.Basket;

import java.math.BigDecimal;

public interface OrderService {

    BigDecimal calculateCost(Basket basket);

    void registerOrder(Basket basket);

}
