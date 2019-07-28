package com.andlvovsky.periodicals.service;

import com.andlvovsky.periodicals.model.publication.Publication;
import com.andlvovsky.periodicals.model.basket.Basket;
import com.andlvovsky.periodicals.model.subscription.Subscription;
import com.andlvovsky.periodicals.model.user.User;
import com.andlvovsky.periodicals.repository.PublicationRepository;
import com.andlvovsky.periodicals.repository.SubscriptionRepository;
import com.andlvovsky.periodicals.service.impl.OrderServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class OrderServiceTests {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private UserService userService;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    private User user = new User(1L, "u", "p", null, null);

    private Publication publication = new Publication("The Guardian", 7, 10., "-");

    @Before
    public void beforeEach() {
        when(userService.getLoggedUser()).thenReturn(user);
    }

    @Test
    public void calculatesBasketCost() {
        Publication publication = new Publication();
        publication.setCost(20.0);
        Basket basket = new Basket(publication, 5);
        double cost = orderService.calculateCost(basket);
        assertEquals(100.0, cost, 0.0);
    }

    @Test
    public void registersOrder() {
        Basket basket = new Basket(publication, 7);
        orderService.registerOrder(basket);
        Subscription subscription = new Subscription(publication, user, 7);
        verify(subscriptionRepository).save(subscription);
    }

}
