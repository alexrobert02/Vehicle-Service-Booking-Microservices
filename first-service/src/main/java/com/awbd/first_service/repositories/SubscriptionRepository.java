package com.awbd.first_service.repositories;

import com.awbd.first_service.model.Subscription;
import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {
    Subscription findByCoachAndSport(String coach, String sport);
}
