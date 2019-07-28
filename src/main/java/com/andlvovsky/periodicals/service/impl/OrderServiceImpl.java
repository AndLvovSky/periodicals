package com.andlvovsky.periodicals.service.impl;

import com.andlvovsky.periodicals.model.publication.Publication;
import com.andlvovsky.periodicals.model.subscription.Basket;
import com.andlvovsky.periodicals.model.subscription.Subscription;
import com.andlvovsky.periodicals.model.user.User;
import com.andlvovsky.periodicals.repository.SubscriptionRepository;
import com.andlvovsky.periodicals.service.OrderService;
import com.andlvovsky.periodicals.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserService userService;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public double calculateCost(Basket basket) {
        Publication publication = basket.getPublication();
        return basket.getNumber() * publication.getCost();
    }

    public void registerOrder(Basket basket) {
        User user = userService.getLoggedUser();
        Publication publication = basket.getPublication();
        Integer number = basket.getNumber();
        Subscription subscription = new Subscription(publication, user, number);
        subscriptionRepository.save(subscription);
    }

}
