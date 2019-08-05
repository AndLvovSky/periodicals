package com.andlvovsky.periodicals.controller;

import com.andlvovsky.periodicals.exception.EmptyBasketException;
import com.andlvovsky.periodicals.exception.PublicationNotFoundException;
import com.andlvovsky.periodicals.meta.ClientPages;
import com.andlvovsky.periodicals.meta.Endpoints;
import com.andlvovsky.periodicals.model.basket.*;
import com.andlvovsky.periodicals.model.money.Money;
import com.andlvovsky.periodicals.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import javax.xml.ws.Endpoint;
import java.util.ArrayList;

@RestController
@SessionAttributes("basket")
@RequiredArgsConstructor
public class BasketController {

    private final OrderService service;

    private final BasketItemMapper itemMapper;

    private final BasketMapper mapper;

    @PostMapping(Endpoints.BASKET_ITEMS)
    public ResponseEntity<String> addItem(
            @Valid @RequestBody BasketItemDto basketItemDto, Errors validationResult, @ModelAttribute Basket basket) {
        if (validationResult.hasErrors()) {
            return new ResponseEntity<>("Invalid basket item", HttpStatus.BAD_REQUEST);
        }
        BasketItem basketItem;
        try {
            basketItem = itemMapper.fromDto(basketItemDto);
        } catch (PublicationNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        basket.getItems().add(basketItem);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(Endpoints.BASKET_ITEMS + "/{index}")
    public ResponseEntity<String> deleteItem(@PathVariable int index, @ModelAttribute Basket basket) {
        try {
            basket.getItems().remove(index - 1);
        } catch (IndexOutOfBoundsException ex) {
            return new ResponseEntity<>("Invalid item index", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(Endpoints.BASKET_ITEMS)
    public void deleteAllItems(@ModelAttribute Basket basket) {
        basket.getItems().clear();
    }

    @GetMapping(Endpoints.BASKET)
    public BasketDto basket(@ModelAttribute Basket basket) {
        return mapper.toDto(basket);
    }

    @GetMapping(Endpoints.BASKET_COST)
    public Money cost(@ModelAttribute Basket basket) {
        return Money.fromDouble(service.calculateCost(basket));
    }

    @PostMapping(Endpoints.BASKET_REGISTRATION)
    public RedirectView registerOrder(@ModelAttribute Basket basket, RedirectAttributes attributes) {
        try {
            service.registerOrder(basket);
        } catch (EmptyBasketException ex) {
            return new RedirectView(ClientPages.BASKET + "?registrationError");
        }
        attributes.addFlashAttribute("basketCost", Money.fromDouble(service.calculateCost(basket)));
        basket.getItems().clear();
        return new RedirectView(ClientPages.REGISTRATION_SUCCESS);
    }

    @ModelAttribute("basket")
    public Basket basket() {
        return new Basket(new ArrayList<>());
    }

}
