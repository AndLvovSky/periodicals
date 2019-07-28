package com.andlvovsky.periodicals.service;

import com.andlvovsky.periodicals.model.publication.Publication;
import com.andlvovsky.periodicals.model.subscription.Basket;
import com.andlvovsky.periodicals.model.subscription.Subscription;
import com.andlvovsky.periodicals.model.user.User;
import com.andlvovsky.periodicals.repository.PublicationRepository;
import com.andlvovsky.periodicals.repository.SubscriptionRepository;
import com.andlvovsky.periodicals.repository.UserRepository;
import com.andlvovsky.periodicals.service.impl.OrderServiceImpl;
import com.andlvovsky.periodicals.service.impl.UserServiceImpl;
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
    private PublicationRepository publicationRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    private User user = new User(1L, "u", "p", null, null);

    private Publication[] publications = {
            new Publication("The Guardian", 7, 10., "-"),
            new Publication("Daily Mail", 1, 5.5, "-")
    };

    @Before
    public void beforeEach() {
        when(userService.getLoggedUser()).thenReturn(user);
        when(publicationRepository.findById(1L)).thenReturn(Optional.ofNullable(publications[0]));
        when(publicationRepository.findById(2L)).thenReturn(Optional.ofNullable(publications[1]));
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
        Basket basket = new Basket(publications[0], 7);
        orderService.registerOrder(basket);
        Subscription subscription = new Subscription(publications[0], user, 7);
        verify(subscriptionRepository).save(subscription);
    }

}
