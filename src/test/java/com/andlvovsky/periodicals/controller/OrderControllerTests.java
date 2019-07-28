package com.andlvovsky.periodicals.controller;

import com.andlvovsky.periodicals.model.basket.Basket;
import com.andlvovsky.periodicals.model.basket.BasketDto;
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

    private Publication publication = new Publication(1L,"The Guardian", 7, 10., "-");

    private Basket basket = new Basket(publication, 3);

    private BasketDto basketDto = new BasketDto(1L, 3);

    @Before
    public void beforeEach() {
        when(orderService.calculateCost(basket)).thenReturn(30.0);
        when(publicationService.getOne(1L)).thenReturn(publication);
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
        assertEquals(30.0, cost.toDouble(), 0.0000000001);
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
