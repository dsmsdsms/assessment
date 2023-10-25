package com.example.sogeti.service;

import com.example.sogeti.model.Category;
import com.example.sogeti.model.Subscription;
import com.example.sogeti.repos.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private SubscriptionService subscriptionService;
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }
    public Map<String, Object> getCategoriesData(String username) {

        List<Category> allCategories = getAllCategories();      // Get all available categories
        List<Subscription> userSubscriptions = subscriptionService.getSubscriptionsByUsername(username); // Get all subscriptions for a given user

        // Convert the user's subscriptions to a list of categories
        List<Map<String, Object>> subscribedCategoriesList = new ArrayList<>();
        for (Subscription subscription : userSubscriptions) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", subscription.getId());
            map.put("name", subscription.getCategory().getName());
            map.put("remainingContent", subscription.getRemainingContent());
            map.put("price", subscription.getPrice());
            map.put("startDate", subscription.getStartDate().toString());
            map.put("startPaymentDate", subscription.getStartPaymentDate().toString());

        if (subscription.getSharedWith() != null) {
            map.put("sharedWith", subscription.getSharedWith().getUsername());
        } else {
            map.put("sharedWith", ""); // if the value is NULL, then inserts an empty string
        }
            subscribedCategoriesList.add(map);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("availableCategories", allCategories);
        response.put("subscribedCategories", subscribedCategoriesList);

        return response;
    }
}
