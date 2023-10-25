package com.example.sogeti.service;

import com.example.sogeti.model.Subscription;
import com.example.sogeti.repos.SubscriptionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepo subscriptionRepo;

    @Autowired
    public SubscriptionServiceImpl(SubscriptionRepo subscriptionRepo) {
        this.subscriptionRepo = subscriptionRepo;
    }

    @Override
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepo.findAll();
    }

    @Override
    public List<Subscription> getSubscriptionsByUsername(String username) {
        return subscriptionRepo.findByUserUsername(username);
    }

    @Override
    public Subscription addSubscription(Subscription subscription) {
        return subscriptionRepo.save(subscription);
    }

    @Override
    public void deleteSubscription(Long id) {
        subscriptionRepo.deleteById(id);
    }

    @Override
    public Optional<Subscription> findById(Long id) {return subscriptionRepo.findById(id);}

    public Subscription updateSubscription(Subscription subscription) {
        return subscriptionRepo.save(subscription);
    }


    @Override
    public List<Subscription> getSubscriptionsByUserOrSharedWith(User user) {
        return subscriptionRepo.findByUserOrSharedWith(user, user);
    }

}
