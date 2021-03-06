package com.andlvovsky.periodicals.service;

import com.andlvovsky.periodicals.dto.BasketDto;
import com.andlvovsky.periodicals.dto.BasketItemDto;
import com.andlvovsky.periodicals.exception.BasketItemNotFoundException;
import com.andlvovsky.periodicals.mapper.BasketItemMapper;
import com.andlvovsky.periodicals.mapper.BasketMapper;
import com.andlvovsky.periodicals.model.basket.*;
import com.andlvovsky.periodicals.model.Publication;
import com.andlvovsky.periodicals.service.impl.BasketServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class BasketServiceTests {

    private BasketServiceImpl basketService;

    @MockBean
    private BasketMapper mapper;

    @MockBean
    private BasketItemMapper itemMapper;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Publication[] publications = {
            new Publication(1L,"The Guardian", 7, new BigDecimal("10"), "-"),
            new Publication(2L, "Daily Mail", 1, new BigDecimal("5"), "-")
    };

    private BasketItem[] basketItems = {
            new BasketItem(publications[0], 3),
            new BasketItem(publications[1], 2)
    };

    private BasketItemDto[] basketItemDtos = {
            new BasketItemDto(1L, 3),
            new BasketItemDto(2L, 2)
    };

    private Basket basket = new Basket();
    {
        for (BasketItem item: basketItems) {
            basket.getItems().add(item);
        }
    }

    private BasketDto basketDto = new BasketDto(Arrays.asList(basketItemDtos));

    @Before
    public void beforeEach() {
        basketService = new BasketServiceImpl(mapper, itemMapper);
        when(mapper.toDto(basket)).thenReturn(basketDto);
        when(itemMapper.fromDto(basketItemDtos[0])).thenReturn(basketItems[0]);
        when(itemMapper.fromDto(basketItemDtos[1])).thenReturn(basketItems[1]);
    }

    @Test
    public void addsItemsToBasket() {
        Basket basket = new Basket();
        basketService.addItem(basket, basketItemDtos[0]);
        basketService.addItem(basket, basketItemDtos[1]);
        assertEquals(this.basket, basket);
    }

    @Test
    public void deletesTheSecondItemFromBasket() {
        Basket basket = new Basket();
        basket.getItems().add(basketItems[0]);
        basket.getItems().add(basketItems[1]);
        basketService.deleteItem(basket, 2);
        assertEquals(1, basket.getItems().size());
        assertEquals(basketItems[0], basket.getItems().get(0));
    }

    @Test
    public void deleteNotExistingItemFromBasketFails() {
        expectedException.expect(BasketItemNotFoundException.class);
        expectedException.expectMessage(containsString("99"));
        Basket basket = new Basket();
        basketService.deleteItem(basket, 99);
    }

    @Test
    public void deletesAllItemsFromBasket() {
        Basket basket = new Basket();
        basket.getItems().add(basketItems[0]);
        basket.getItems().add(basketItems[1]);
        basketService.deleteAllItems(basket);
        assertEquals(0, basket.getItems().size());
    }

    @Test
    public void getsBasket() {
        BasketDto basketDto = basketService.getBasket(basket);
        assertEquals(this.basketDto, basketDto);
    }

}
