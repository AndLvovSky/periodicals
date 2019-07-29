package com.andlvovsky.periodicals.service;

import com.andlvovsky.periodicals.model.basket.BasketItem;
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

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class OrderServiceTests {

    @InjectMocks
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

    @Before
    public void beforeEach() {
        when(userService.getLoggedUser()).thenReturn(user);
    }

    @Test
    public void calculatesBasketCost() {
        double cost = orderService.calculateCost(basket);
        assertEquals(100.0, cost, 0.0);
    }

    @Test
    public void registersOrder() {
        orderService.registerOrder(basket);
        Subscription[] subscriptions = {
                new Subscription(publications[0], user, 5),
                new Subscription(publications[1], user, 10)
        };
        verify(subscriptionRepository).save(subscriptions[0]);
        verify(subscriptionRepository).save(subscriptions[1]);
        verifyNoMoreInteractions(subscriptionRepository);
    }

}
