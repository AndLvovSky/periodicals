package com.andlvovsky.periodicals.repository;

import com.andlvovsky.periodicals.model.subscription.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

}
