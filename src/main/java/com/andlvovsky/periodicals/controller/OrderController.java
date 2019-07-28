package com.andlvovsky.periodicals.controller;

import com.andlvovsky.periodicals.model.basket.Basket;
import com.andlvovsky.periodicals.model.basket.BasketDto;
import com.andlvovsky.periodicals.model.basket.BasketMapper;
import com.andlvovsky.periodicals.model.money.Money;
import com.andlvovsky.periodicals.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService service;

    @Autowired
    private BasketMapper mapper;

    @PostMapping("/cost")
    @ResponseBody
    public Money cost(@RequestBody BasketDto basketDto) {
        Basket basket = mapper.fromDto(basketDto);
        return Money.fromDouble(service.calculateCost(basket));
    }

    @PostMapping("/register")
    @ResponseBody
    public void registerOrder(@RequestBody BasketDto basketDto) {
        Basket basket = mapper.fromDto(basketDto);
        service.registerOrder(basket);
    }

}
