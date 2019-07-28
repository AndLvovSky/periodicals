package com.andlvovsky.periodicals.service;

import com.andlvovsky.periodicals.model.subscription.Basket;

public interface OrderService {

    double calculateCost(Basket basket);

    void registerOrder(Basket basket);

}
