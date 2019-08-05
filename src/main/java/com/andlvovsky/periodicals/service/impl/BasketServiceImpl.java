package com.andlvovsky.periodicals.service.impl;

import com.andlvovsky.periodicals.dto.BasketDto;
import com.andlvovsky.periodicals.dto.BasketItemDto;
import com.andlvovsky.periodicals.exception.BasketItemNotFoundException;
import com.andlvovsky.periodicals.mapper.BasketItemMapper;
import com.andlvovsky.periodicals.mapper.BasketMapper;
import com.andlvovsky.periodicals.model.basket.*;
import com.andlvovsky.periodicals.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasketServiceImpl implements BasketService {

    private final BasketMapper mapper;

    private final BasketItemMapper itemMapper;

    public void addItem(Basket basket, BasketItemDto basketItemDto) {
        BasketItem basketItem;
        basketItem = itemMapper.fromDto(basketItemDto);
        basket.getItems().add(basketItem);
    }

    public void deleteItem(Basket basket, int index) {
        try {
            basket.getItems().remove(index - 1);
        } catch (IndexOutOfBoundsException ex) {
            throw new BasketItemNotFoundException(index);
        }
    }

    public void deleteAllItems(Basket basket) {
        basket.getItems().clear();
    }

    public BasketDto getBasket(Basket basket) {
        return mapper.toDto(basket);
    }

}
