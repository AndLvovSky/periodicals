package com.andlvovsky.periodicals.controller;

import com.andlvovsky.periodicals.model.basket.*;
import com.andlvovsky.periodicals.model.money.Money;
import com.andlvovsky.periodicals.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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
    public RedirectView registerOrder(@ModelAttribute Basket basket, RedirectAttributes attributes) {
        service.registerOrder(basket);
        attributes.addFlashAttribute("basketCost", Money.fromDouble(service.calculateCost(basket)));
        basket.getItems().clear();
        return new RedirectView("/register-success");
    }

    @ModelAttribute("basket")
    public Basket basket() {
        return new Basket(new ArrayList<>());
    }

}
