package com.andlvovsky.periodicals.controller;

import com.andlvovsky.periodicals.model.basket.*;
import com.andlvovsky.periodicals.model.money.Money;
import com.andlvovsky.periodicals.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/order")
@SessionAttributes("basket")
public class OrderController {

    @Autowired
    private OrderService service;

    @Autowired
    private BasketItemMapper itemMapper;

    @Autowired
    private BasketMapper mapper;

    @PostMapping("/add")
    @ResponseBody
    public void addItem(@RequestBody BasketItemDto basketItemDto, @ModelAttribute Basket basket) {
        BasketItem basketItem = itemMapper.fromDto(basketItemDto);
        basket.getItems().add(basketItem);
    }

    @GetMapping("/basket")
    @ResponseBody
    public BasketDto basket(@ModelAttribute Basket basket) {
        return mapper.toDto(basket);
    }

    @GetMapping("/cost")
    @ResponseBody
    public Money cost(@ModelAttribute Basket basket) {
        return Money.fromDouble(service.calculateCost(basket));
    }

    @PostMapping("/register")
    @ResponseBody
    public void registerOrder(@ModelAttribute Basket basket) {
        service.registerOrder(basket);
    }

    @ModelAttribute("basket")
    public Basket basket() {
        return new Basket(new ArrayList<>());
    }

}
