package com.example.sogeti.service;

import com.example.sogeti.model.Subscription;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Optional;

public interface SubscriptionService {
    List<Subscription> getAllSubscriptions();
    List<Subscription> getSubscriptionsByUsername(String username);
    Subscription addSubscription(Subscription subscription);
    void deleteSubscription(Long id);
    Optional<Subscription> findById(Long id);
    Subscription updateSubscription(Subscription subscription);
    List<Subscription> getSubscriptionsByUserOrSharedWith(User user);

}
