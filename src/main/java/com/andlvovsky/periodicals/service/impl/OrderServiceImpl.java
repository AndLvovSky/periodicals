package com.andlvovsky.periodicals.service.impl;

import com.andlvovsky.periodicals.exception.EmptyBasketException;
import com.andlvovsky.periodicals.model.Publication;
import com.andlvovsky.periodicals.model.basket.Basket;
import com.andlvovsky.periodicals.model.Subscription;
import com.andlvovsky.periodicals.model.basket.BasketItem;
import com.andlvovsky.periodicals.model.user.User;
import com.andlvovsky.periodicals.repository.SubscriptionRepository;
import com.andlvovsky.periodicals.service.OrderService;
import com.andlvovsky.periodicals.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserService userService;

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public BigDecimal calculateCost(Basket basket) {
        BigDecimal sum = BigDecimal.ZERO;
        for (BasketItem item : basket.getItems()) {
            Publication publication = item.getPublication();
            BigDecimal itemCost = publication.getCost().multiply(BigDecimal.valueOf((long)item.getNumber()));
            sum = sum.add(itemCost);
        }
        return sum;
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
