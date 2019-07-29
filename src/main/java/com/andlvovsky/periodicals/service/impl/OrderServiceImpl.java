package com.andlvovsky.periodicals.service.impl;

import com.andlvovsky.periodicals.model.basket.BasketItem;
import com.andlvovsky.periodicals.model.publication.Publication;
import com.andlvovsky.periodicals.model.basket.Basket;
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
        double cost = 0;
        for (BasketItem item : basket.getItems()) {
            Publication publication = item.getPublication();
            cost += item.getNumber() * publication.getCost();
        }
        return cost;

    }

    public void registerOrder(Basket basket) {
        User user = userService.getLoggedUser();
        for (BasketItem item : basket.getItems()) {
            Publication publication = item.getPublication();
            Integer number = item.getNumber();
            Subscription subscription = new Subscription(publication, user, number);
            subscriptionRepository.save(subscription);
        }
    }

}
