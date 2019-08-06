package com.andlvovsky.periodicals.controller;

import com.andlvovsky.periodicals.dto.BasketDto;
import com.andlvovsky.periodicals.dto.BasketItemDto;
import com.andlvovsky.periodicals.exception.BasketItemNotFoundException;
import com.andlvovsky.periodicals.exception.EmptyBasketException;
import com.andlvovsky.periodicals.exception.PublicationNotFoundException;
import com.andlvovsky.periodicals.meta.ClientPages;
import com.andlvovsky.periodicals.meta.Endpoints;
import com.andlvovsky.periodicals.model.basket.*;
import com.andlvovsky.periodicals.dto.Money;
import com.andlvovsky.periodicals.model.Publication;
import com.andlvovsky.periodicals.service.BasketService;
import com.andlvovsky.periodicals.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BasketController.class)
@AutoConfigureMockMvc
@WithMockUser(authorities = {"READ_PUBLICATIONS"})
public class BasketControllerTests extends ControllerTests {

    @MockBean
    private BasketService basketService;

    @MockBean
    private OrderService orderService;

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

    private Basket basket = new Basket();
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

    private Map<String, Object> sessionAttrEmpty = new HashMap<>();
    {
        sessionAttrEmpty.put("basket", new Basket());
    }

    @Before
    public void beforeEach() {
        when(orderService.calculateCost(basket)).thenReturn(Money.fromDouble(50.0));
        doThrow(new EmptyBasketException()).when(orderService).registerOrder(new Basket());
        when(basketService.getBasket(basket)).thenReturn(basketDto);
        doThrow(new PublicationNotFoundException(99L)).when(basketService).addItem(
                any(Basket.class), eq(basketItemDtoNotExistingPublication));
        doThrow(new BasketItemNotFoundException(88)).when(basketService).deleteItem(any(Basket.class), eq(88));
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
            mvc.perform(post(Endpoints.BASKET_ITEMS).session(session)
                    .content(basketItemJson).contentType(MediaType.APPLICATION_JSON_UTF8)).andDo(print());
        }
        verify(basketService).addItem(any(Basket.class), eq(basketItemDtos[0]));
        verify(basketService).addItem(any(Basket.class), eq(basketItemDtos[1]));
        verifyNoMoreInteractions(basketService);
    }

    @Test
    public void addItemToBasketFailsNotExistingPublication() throws Exception {
        String basketItemJson = jsonMapper.writeValueAsString(basketItemDtoNotExistingPublication);
        mvc.perform(post(Endpoints.BASKET_ITEMS).sessionAttrs(sessionAttrEmpty)
                .content(basketItemJson).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(content().string(new PublicationNotFoundException(99L).getMessage()))
                .andDo(print());
    }

    @Test
    public void addItemToBasketFailsNotPositiveNumber() throws Exception {
        String basketItemJson = jsonMapper.writeValueAsString(basketItemDtoNotPositiveNumber);
        mvc.perform(post(Endpoints.BASKET_ITEMS).sessionAttrs(sessionAttrEmpty)
                .content(basketItemJson).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid basket item"))
                .andDo(print());
    }

    @Test
    public void deletesTheSecondItemFromBasket() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("basket", basket);
        mvc.perform(delete(Endpoints.BASKET_ITEMS  + "/2").session(session)).andDo(print());
        verify(basketService).deleteItem(any(Basket.class), eq(2));
    }

    @Test
    public void deleteItemFromBasketFailsInvalidIndex() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("basket", basket);
        mvc.perform(delete(Endpoints.BASKET_ITEMS  + "/88").session(session))
                .andExpect(status().isNotFound())
                .andExpect(content().string(new BasketItemNotFoundException(88).getMessage()))
                .andDo(print());
    }

    @Test
    public void deletesAllItemsFromBasket() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("basket", basket);
        mvc.perform(delete(Endpoints.BASKET_ITEMS).session(session)).andDo(print());
        verify(basketService).deleteAllItems(any(Basket.class));
    }

}
