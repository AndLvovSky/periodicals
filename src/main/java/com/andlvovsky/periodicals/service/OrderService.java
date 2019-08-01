package com.andlvovsky.periodicals.service;

import com.andlvovsky.periodicals.model.basket.Basket;

public interface OrderService {

    double calculateCost(Basket basket);

    void registerOrder(Basket basket);

}
