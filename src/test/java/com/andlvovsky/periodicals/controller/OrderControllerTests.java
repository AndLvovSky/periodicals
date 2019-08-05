package com.andlvovsky.periodicals.controller;

import com.andlvovsky.periodicals.exception.EmptyBasketException;
import com.andlvovsky.periodicals.exception.PublicationNotFoundException;
import com.andlvovsky.periodicals.meta.ClientPages;
import com.andlvovsky.periodicals.meta.Endpoints;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    private BasketItemDto basketItemDtoNotExistingPublication = new BasketItemDto(99L, 5);

    private BasketItemDto basketItemDtoNotPositiveNumber = new BasketItemDto(1L, -7);

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

    private Basket emptyBasket = new Basket(new ArrayList<>());

    private BasketDto basketDto = new BasketDto(Arrays.asList(basketItemDtos));

    private Map<String, Object> sessionAttr = new HashMap<>();
    {
        sessionAttr.put("basket", basket);
    }

    private Map<String, Object> sessionAttrEmpty = new HashMap<>();
    {
        sessionAttrEmpty.put("basket", emptyBasket);
    }

    @Before
    public void beforeEach() {
        when(orderService.calculateCost(basket)).thenReturn(50.0);
        when(publicationService.getOne(1L)).thenReturn(publications[0]);
        when(publicationService.getOne(2L)).thenReturn(publications[1]);
        when(basketMapper.toDto(basket)).thenReturn(basketDto);
        when(basketItemMapper.fromDto(basketItemDtos[0])).thenReturn(basketItems[0]);
        when(basketItemMapper.fromDto(basketItemDtos[1])).thenReturn(basketItems[1]);
        doThrow(new PublicationNotFoundException(99L))
                .when(basketItemMapper).fromDto(basketItemDtoNotExistingPublication);
        doThrow(new EmptyBasketException()).when(orderService).registerOrder(emptyBasket);
    }

    @Test
    public void returnsBasketCost() throws Exception {
        MvcResult result = mvc.perform(get(Endpoints.BASKET_COST).sessionAttrs(sessionAttr))
                .andExpect(status().isOk()).andDo(print())
                .andReturn();
        String jsonContent = result.getResponse().getContentAsString();
        Money cost = jsonMapper.readValue(jsonContent, Money.class);
        assertEquals(50.0, cost.toDouble(), 0.0000000001);
    }

    @Test
    public void registersOrder() throws Exception {
        mvc.perform(post(Endpoints.BASKET_REGISTRATION).sessionAttrs(sessionAttr))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ClientPages.REGISTRATION_SUCCESS)).andDo(print());
        verify(orderService, times(1)).registerOrder(basket);
    }

    @Test
    public void registerOrderFailsEmptyBasket() throws Exception {
        mvc.perform(post(Endpoints.BASKET_REGISTRATION).sessionAttrs(sessionAttrEmpty))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ClientPages.BASKET + "?registrationError")).andDo(print());
        verify(orderService, times(0)).registerOrder(basket);
    }

    @Test
    public void returnsBasket() throws Exception {
        MvcResult result = mvc.perform(get(Endpoints.BASKET).sessionAttrs(sessionAttr))
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
            mvc.perform(post(Endpoints.BASKET_ITEMS  + "/add").session(session)
                    .content(basketItemJson).contentType(MediaType.APPLICATION_JSON_UTF8)).andDo(print());
        }
        assertEquals(basket, session.getAttribute("basket"));
    }

    @Test
    public void addItemToBasketFailsNotExistingPublication() throws Exception {
        String basketItemJson = jsonMapper.writeValueAsString(basketItemDtoNotExistingPublication);
        mvc.perform(post(Endpoints.BASKET_ITEMS  + "/add").sessionAttrs(sessionAttrEmpty)
                .content(basketItemJson).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new PublicationNotFoundException(99L).getMessage()))
                .andDo(print());
    }

    @Test
    public void addItemToBasketFailsNotPositiveNumber() throws Exception {
        String basketItemJson = jsonMapper.writeValueAsString(basketItemDtoNotPositiveNumber);
        mvc.perform(post(Endpoints.BASKET_ITEMS  + "/add").sessionAttrs(sessionAttrEmpty)
                .content(basketItemJson).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid basket item"))
                .andDo(print());
    }

    @Test
    public void deletesTheSecondItemFromBasket() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("basket", basket);
        mvc.perform(delete(Endpoints.BASKET_ITEMS  + "/delete/2").session(session)).andDo(print());
        Basket basket = (Basket)session.getAttribute("basket");
        assertEquals(1, basket.getItems().size());
        assertEquals((Integer)3, basket.getItems().get(0).getNumber());
    }

    @Test
    public void deleteItemFromBasketFailsInvalidIndex() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("basket", basket);
        mvc.perform(delete(Endpoints.BASKET_ITEMS  + "/delete/88").session(session))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid item index"))
                .andDo(print());
    }

    @Test
    public void deletesAllItemsFromBasket() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("basket", basket);
        mvc.perform(delete(Endpoints.BASKET_ITEMS  + "/delete").session(session)).andDo(print());
        Basket basket = (Basket)session.getAttribute("basket");
        assertEquals(0, basket.getItems().size());
    }

}
