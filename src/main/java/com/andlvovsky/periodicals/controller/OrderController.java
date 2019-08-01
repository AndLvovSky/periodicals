package com.andlvovsky.periodicals.controller;

import com.andlvovsky.periodicals.exception.EmptyBasketException;
import com.andlvovsky.periodicals.exception.PublicationNotFoundException;
import com.andlvovsky.periodicals.model.basket.*;
import com.andlvovsky.periodicals.model.money.Money;
import com.andlvovsky.periodicals.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
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

    @DeleteMapping("/delete/{index}")
    public ResponseEntity<String> deleteItem(@PathVariable int index, @ModelAttribute Basket basket) {
        try {
            basket.getItems().remove(index - 1);
        } catch (IndexOutOfBoundsException ex) {
            return new ResponseEntity<>("Invalid item index", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/delete/")
    public void deleteAllItems(@ModelAttribute Basket basket) {
        basket.getItems().clear();
    }

    @GetMapping("/basket")
    public BasketDto basket(@ModelAttribute Basket basket) {
        return mapper.toDto(basket);
    }

    @GetMapping("/cost")
    public Money cost(@ModelAttribute Basket basket) {
        return Money.fromDouble(service.calculateCost(basket));
    }

    @PostMapping("/register")
    public RedirectView registerOrder(@ModelAttribute Basket basket, RedirectAttributes attributes) {
        try {
            service.registerOrder(basket);
        } catch (EmptyBasketException ex) {
            return new RedirectView("/basket?registrationError");
        }
        attributes.addFlashAttribute("basketCost", Money.fromDouble(service.calculateCost(basket)));
        basket.getItems().clear();
        return new RedirectView("/register-success");
    }

    @ModelAttribute("basket")
    public Basket basket() {
        return new Basket(new ArrayList<>());
    }

}
