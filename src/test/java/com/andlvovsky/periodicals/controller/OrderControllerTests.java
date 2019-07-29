package com.andlvovsky.periodicals.controller;

import com.andlvovsky.periodicals.model.basket.Basket;
import com.andlvovsky.periodicals.model.basket.BasketDto;
import com.andlvovsky.periodicals.model.basket.BasketItem;
import com.andlvovsky.periodicals.model.basket.BasketItemDto;
import com.andlvovsky.periodicals.model.money.Money;
import com.andlvovsky.periodicals.model.publication.Publication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
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

    private Basket basket = new Basket(Arrays.asList(
            new BasketItem(publications[0], 3),
            new BasketItem(publications[1], 2)
    ));

    private BasketDto basketDto = new BasketDto(Arrays.asList(
            new BasketItemDto(1L, 3),
            new BasketItemDto(2L, 2)
    ));

    @Before
    public void beforeEach() {
        when(orderService.calculateCost(basket)).thenReturn(50.0);
        when(publicationService.getOne(1L)).thenReturn(publications[0]);
        when(publicationService.getOne(2L)).thenReturn(publications[1]);
        when(basketMapper.fromDto(basketDto)).thenReturn(basket);
    }

    @Test
    public void returnsBasketCost() throws Exception {
        String basketJson = jsonMapper.writeValueAsString(basketDto);
        MvcResult result =
                mvc.perform(post(url("/cost")).content(basketJson).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andDo(print())
                .andReturn();
        String jsonContent = result.getResponse().getContentAsString();
        Money cost = jsonMapper.readValue(jsonContent, Money.class);
        assertEquals(50.0, cost.toDouble(), 0.0000000001);
    }

    @Test
    public void registersOrder() throws Exception {
        String basketJson = jsonMapper.writeValueAsString(basketDto);
        mvc.perform(post(url("/register")).content(basketJson).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andDo(print());
        verify(orderService).registerOrder(basket);
        verifyNoMoreInteractions(orderService);
    }

    private String url(String suffix) {
        return "/order" + suffix;
    }

}
