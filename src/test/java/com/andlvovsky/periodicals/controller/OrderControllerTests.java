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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
@AutoConfigureMockMvc
@WithMockUser
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

    private Basket basket = new Basket(Arrays.asList(basketItems));

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
                .andExpect(status().isOk()).andDo(print());
        verify(orderService).registerOrder(basket);
        verifyNoMoreInteractions(orderService);
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
                    .content(basketItemJson).contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
        }
        MvcResult result = mvc.perform(get(url("/basket")).session(session))
                .andExpect(status().isOk()).andDo(print())
                .andReturn();
        String jsonContent = result.getResponse().getContentAsString();
        BasketDto actualBasketDto = jsonMapper.readValue(jsonContent, BasketDto.class);
        assertEquals(basketDto, actualBasketDto);
    }

    private String url(String suffix) {
        return "/order" + suffix;
    }

}
