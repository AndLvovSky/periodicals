package com.andlvovsky.periodicals.controller;


import com.andlvovsky.periodicals.dto.BasketDto;
import com.andlvovsky.periodicals.dto.BasketItemDto;
import com.andlvovsky.periodicals.meta.ClientPages;
import com.andlvovsky.periodicals.meta.Endpoints;
import com.andlvovsky.periodicals.model.basket.*;
import com.andlvovsky.periodicals.model.Money;
import com.andlvovsky.periodicals.service.BasketService;
import com.andlvovsky.periodicals.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;

@RestController
@SessionAttributes("basket")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    private final OrderService orderService;

    @PostMapping(Endpoints.BASKET_ITEMS)
    public ResponseEntity<String> addItem(
            @Valid @RequestBody BasketItemDto basketItemDto, Errors validationResult, @ModelAttribute Basket basket) {
        if (validationResult.hasErrors()) {
            return new ResponseEntity<>("Invalid basket item", HttpStatus.BAD_REQUEST);
        }
        basketService.addItem(basket, basketItemDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(Endpoints.BASKET_ITEMS + "/{index}")
    public ResponseEntity<String> deleteItem(@PathVariable int index, @ModelAttribute Basket basket) {
        basketService.deleteItem(basket, index);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(Endpoints.BASKET_ITEMS)
    public void deleteAllItems(@ModelAttribute Basket basket) {
        basketService.deleteAllItems(basket);
    }

    @GetMapping(Endpoints.BASKET)
    public BasketDto basket(@ModelAttribute Basket basket) {
        return basketService.getBasket(basket);
    }

    @GetMapping(Endpoints.BASKET_COST)
    public Money cost(@ModelAttribute Basket basket) {
        return Money.fromDouble(orderService.calculateCost(basket));
    }

    @PostMapping(Endpoints.BASKET_REGISTRATION)
    public RedirectView registerOrder(@ModelAttribute Basket basket, RedirectAttributes attributes) {
        orderService.registerOrder(basket);
        attributes.addFlashAttribute("basketCost", Money.fromDouble(orderService.calculateCost(basket)));
        basketService.deleteAllItems(basket);
        return new RedirectView(ClientPages.REGISTRATION_SUCCESS);
    }

    @ModelAttribute("basket")
    public Basket basket() {
        return new Basket();
    }

}
