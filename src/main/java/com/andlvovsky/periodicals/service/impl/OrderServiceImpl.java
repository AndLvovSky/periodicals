package com.andlvovsky.periodicals.service.impl;

import com.andlvovsky.periodicals.dto.Money;
import com.andlvovsky.periodicals.exception.EmptyBasketException;
import com.andlvovsky.periodicals.model.Publication;
import com.andlvovsky.periodicals.model.basket.Basket;
import com.andlvovsky.periodicals.model.Subscription;
import com.andlvovsky.periodicals.model.user.User;
import com.andlvovsky.periodicals.repository.SubscriptionRepository;
import com.andlvovsky.periodicals.service.OrderService;
import com.andlvovsky.periodicals.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserService userService;

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public Money calculateCost(Basket basket) {
        double cost = basket.getItems().stream()
                .mapToDouble(item -> {
                    Publication publication = item.getPublication();
                    return item.getNumber() * publication.getCost();
                }).sum();
        return Money.fromDouble(cost);
    }

    @Override
    @Transactional
    public void registerOrder(Basket basket) {
        if (basket.getItems().isEmpty()) {
            throw new EmptyBasketException();
        }
        User user = userService.getLoggedUser();
        basket.getItems()
                .forEach(item -> {
                    Publication publication = item.getPublication();
                    Integer number = item.getNumber();
                    Subscription subscription = new Subscription(publication, user, number);
                    subscriptionRepository.save(subscription);
                });
    }

}
