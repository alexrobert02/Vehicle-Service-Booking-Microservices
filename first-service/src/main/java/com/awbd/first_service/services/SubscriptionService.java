package com.awbd.first_service.services;


import com.awbd.first_service.model.Subscription;

import java.util.List;

public interface SubscriptionService {
    Subscription findByCoachAndSport(String coach, String sport);
    Subscription save(Subscription subscription);
    List<Subscription> findAll();
    boolean delete(Long id);
    Subscription findById(Long id);
}
