package com.andlvovsky.periodicals.controller;

import com.andlvovsky.periodicals.model.basket.BasketItemMapper;
import com.andlvovsky.periodicals.model.basket.BasketMapper;
import com.andlvovsky.periodicals.service.OrderService;
import com.andlvovsky.periodicals.service.PublicationService;
import com.andlvovsky.periodicals.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

public class ControllerTests {

    @Autowired
    protected MockMvc mvc;

    @MockBean
    protected PublicationService publicationService;

    @MockBean
    protected OrderService orderService;

    @MockBean
    protected UserService userService;

    @MockBean
    protected UserDetailsService userDetailsService;

    @MockBean
    protected BasketMapper basketMapper;

    @MockBean
    protected BasketItemMapper basketItemMapper;

}
