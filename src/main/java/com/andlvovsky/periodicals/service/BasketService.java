package com.andlvovsky.periodicals.service;

import com.andlvovsky.periodicals.model.basket.Basket;
import com.andlvovsky.periodicals.model.basket.BasketDto;
import com.andlvovsky.periodicals.model.basket.BasketItemDto;

public interface BasketService {

    void addItem(Basket basket, BasketItemDto basketItemDto);

    void deleteItem(Basket basket, int index);

    void deleteAllItems(Basket basket);

    BasketDto getBasket(Basket basket);

}
