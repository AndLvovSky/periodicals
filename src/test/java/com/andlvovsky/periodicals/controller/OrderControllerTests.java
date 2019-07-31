package com.andlvovsky.periodicals.controller;

import com.andlvovsky.periodicals.model.basket.*;
import com.andlvovsky.periodicals.model.money.Money;
import com.andlvovsky.periodicals.model.publication.Publication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
@AutoConfigureMockMvc
@WithMockUser(authorities = {"READ_PUBLICATIONS"})
public class OrderControllerTests extends ControllerTests {

    private ObjectMapper jsonMapper = new ObjectMapper();

    private Publication[] publications = {
            new Publication(1L,"The Guardian", 7, 10., "-"),
            new Publication(2L, "Daily Mail", 1, 5., "-")
    };

    private BasketItem[] basketItems = {
            new BasketItem(publications[0], 3),
            new BasketItem(publications[1], 2)
    };

    private BasketItemDto[] basketItemDtos = {
            new BasketItemDto(1L, 3),
            new BasketItemDto(2L, 2)
    };

    private Basket basket = new Basket(new ArrayList<>());
    {
        for (BasketItem item: basketItems) {
            basket.getItems().add(item);
        }
    }

    private BasketDto basketDto = new BasketDto(Arrays.asList(basketItemDtos));

    private Map<String, Object> sessionAttr = new HashMap<>();
    {
        sessionAttr.put("basket", basket);
    }

    @Before
    public void beforeEach() {
        when(orderService.calculateCost(basket)).thenReturn(50.0);
        when(publicationService.getOne(1L)).thenReturn(publications[0]);
        when(publicationService.getOne(2L)).thenReturn(publications[1]);
        when(basketMapper.toDto(basket)).thenReturn(basketDto);
        when(basketItemMapper.fromDto(basketItemDtos[0])).thenReturn(basketItems[0]);
        when(basketItemMapper.fromDto(basketItemDtos[1])).thenReturn(basketItems[1]);
    }

    @Test
    public void returnsBasketCost() throws Exception {
        MvcResult result = mvc.perform(get(url("/cost")).sessionAttrs(sessionAttr))
                .andExpect(status().isOk()).andDo(print())
                .andReturn();
        String jsonContent = result.getResponse().getContentAsString();
        Money cost = jsonMapper.readValue(jsonContent, Money.class);
        assertEquals(50.0, cost.toDouble(), 0.0000000001);
    }

    @Test
    public void registersOrder() throws Exception {
        mvc.perform(post(url("/register")).sessionAttrs(sessionAttr))
                .andExpect(status().is3xxRedirection()).andDo(print());
        verify(orderService, times(1)).registerOrder(basket);
    }

    @Test
    public void returnsBasket() throws Exception {
        MvcResult result = mvc.perform(get(url("/basket")).sessionAttrs(sessionAttr))
                .andExpect(status().isOk()).andDo(print())
                .andReturn();
        String jsonContent = result.getResponse().getContentAsString();
        BasketDto actualBasketDto = jsonMapper.readValue(jsonContent, BasketDto.class);
        assertEquals(basketDto, actualBasketDto);
    }

    @Test
    public void addsItemsToBasket() throws Exception {
        MockHttpSession session = new MockHttpSession();
        for (BasketItemDto item : basketDto.getItems()) {
            String basketItemJson = jsonMapper.writeValueAsString(item);
            mvc.perform(post(url("/add")).session(session)
                    .content(basketItemJson).contentType(MediaType.APPLICATION_JSON_UTF8)).andDo(print());
        }
        assertEquals(basket, session.getAttribute("basket"));
    }

    @Test
    public void deletesTheSecondItemFromBasket() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("basket", basket);
        mvc.perform(delete(url("/delete/2")).session(session)).andDo(print());
        Basket basket = (Basket)session.getAttribute("basket");
        assertEquals(1, basket.getItems().size());
        assertEquals((Integer)3, basket.getItems().get(0).getNumber());
    }

    @Test
    public void deletesAllItemsFromBasket() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("basket", basket);
        mvc.perform(delete(url("/delete/")).session(session)).andDo(print());
        Basket basket = (Basket)session.getAttribute("basket");
        assertEquals(0, basket.getItems().size());
    }

    private String url(String suffix) {
        return "/order" + suffix;
    }

}
