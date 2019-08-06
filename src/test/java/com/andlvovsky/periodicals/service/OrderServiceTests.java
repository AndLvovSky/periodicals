package com.andlvovsky.periodicals.service;

import com.andlvovsky.periodicals.dto.Money;
import com.andlvovsky.periodicals.exception.EmptyBasketException;
import com.andlvovsky.periodicals.model.basket.BasketItem;
import com.andlvovsky.periodicals.model.Publication;
import com.andlvovsky.periodicals.model.basket.Basket;
import com.andlvovsky.periodicals.model.Subscription;
import com.andlvovsky.periodicals.model.user.User;
import com.andlvovsky.periodicals.repository.SubscriptionRepository;
import com.andlvovsky.periodicals.service.impl.OrderServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class OrderServiceTests {

    private OrderServiceImpl orderService;

    @Mock
    private UserService userService;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    private User user = new User(1L, "u", "p", null, null);

    private Publication[] publications = {
            new Publication(1L,"The Guardian", 7, 10., "-"),
            new Publication(2L, "Daily Mail", 1, 5., "-")
    };

    private Basket basket = new Basket(Arrays.asList(
            new BasketItem(publications[0], 5),
            new BasketItem(publications[1], 10)
    ));

    private Subscription[] subscriptions = {
            new Subscription(publications[0], user, 5),
            new Subscription(publications[1], user, 10)
    };

    @Before
    public void beforeEach() {
        orderService = new OrderServiceImpl(userService, subscriptionRepository);
        when(userService.getLoggedUser()).thenReturn(user);
    }

    @Test
    public void calculatesBasketCost() {
        Money cost = orderService.calculateCost(basket);
        assertEquals(100, cost.toDouble(), 0.0);
    }

    @Test
    public void calculatesEmptyBasketCost() {
        Money cost = orderService.calculateCost(new Basket());
        assertEquals(0.0, cost.toDouble(), 0.0);
    }

    @Test
    public void registersOrder() {
        orderService.registerOrder(basket);
        verify(subscriptionRepository).save(subscriptions[0]);
        verify(subscriptionRepository).save(subscriptions[1]);
        verifyNoMoreInteractions(subscriptionRepository);
    }

    @Test(expected = EmptyBasketException.class)
    public void registerOrderFailsEmptyBasket() {
        try {
            orderService.registerOrder(new Basket());
        } finally {
            verifyNoMoreInteractions(subscriptionRepository);
        }
    }

}
