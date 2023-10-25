package com.example.sogeti.controller;

import com.example.sogeti.model.Category;
import com.example.sogeti.model.Subscription;
import com.example.sogeti.model.User;
import com.example.sogeti.repos.CategoryRepo;
import com.example.sogeti.service.CategoryService;
import com.example.sogeti.service.SubscriptionService;
import com.example.sogeti.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.Optional;

@Controller
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
    private final CategoryRepo categoryRepo;
    private final SubscriptionService subscriptionService;
    private final UserService userService;

    @Autowired
    public SubscriptionController(
            CategoryRepo categoryRepo,
            SubscriptionService subscriptionService,
            UserService userService
    ) {
        this.categoryRepo = categoryRepo;
        this.subscriptionService = subscriptionService;
        this.userService = userService;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribeToCategory(
            @RequestParam String categoryName,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // Get the authenticated username
        String username = userDetails.getUsername();


        // Check if the user is already subscribed to this category
        List<Subscription> userSubscriptions = subscriptionService.getSubscriptionsByUsername(username);
        boolean isAlreadySubscribed = userSubscriptions.stream().anyMatch(sub ->
                sub.getCategory().getName().equalsIgnoreCase(categoryName)
        );
        if (isAlreadySubscribed) {
            System.out.println("You are already subscribed to this category");
            return ResponseEntity.badRequest().body("You are already subscribed to this category");
        }

            System.out.println("Searching for category: " + categoryName);
        // Find a category by name
        Optional<Category> categoryOptional = categoryRepo.findByName(categoryName);
            System.out.println("Found category: " + categoryOptional);

        if (!categoryOptional.isPresent()) {
            System.out.println("Category not found");
            return ResponseEntity.badRequest().body("Category not found");
        }
        Category category = categoryOptional.get();

        // Create a subscription for the user for this category
        User user = userService.findByUsername(username);
        Subscription subscription = new Subscription(user, category,category.getAvailableContent(),
                category.getPrice(),LocalDate.now());

        subscription.setStartPaymentDate(LocalDate.now().plusMonths(1)); // подписка на 1 месяц

        System.out.println("Remaining Content: " + subscription.getRemainingContent());

        // Add a new subscription
        subscriptionService.addSubscription(subscription);
        System.out.println("Subscription completed successfully");
        return ResponseEntity.ok("Subscription completed successfully");
    }

    @Autowired
    private CategoryService categoryService;
    @GetMapping("/categories")
    public ResponseEntity<?> getAvailableAndSubscribedCategories(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        Map<String, Object> categoriesData = categoryService.getCategoriesData(username);

        return ResponseEntity.ok(categoriesData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> unsubscribe(@PathVariable Long id) {
        try {
            subscriptionService.deleteSubscription(id);
            System.out.println("Subscription successfully deleted");
            return ResponseEntity.ok("Subscription successfully deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while canceling your subscription");
        }
    }

    @PostMapping("/share")
    public ResponseEntity<?> shareSubscription(
            @RequestParam Long subscriptionId,
            @RequestParam String shareWithUsername,
            @AuthenticationPrincipal UserDetails currentUserDetails
    ) {

        // -----------  Checking share conditions ----------------
        if (currentUserDetails.getUsername().equals(shareWithUsername)) {
            System.out.println("You cannot share a subscription with yourself: " + shareWithUsername);
            return ResponseEntity.badRequest().body("You cannot share a subscription with yourself.");
        }

        Optional<Subscription> subscriptionOptional = subscriptionService.findById(subscriptionId);
        if (subscriptionOptional.isEmpty()) {
            System.out.println("The subscription was not found or you are not the owner.");
            return ResponseEntity.badRequest().body("The subscription was not found or you are not the owner.");
        }
        Subscription subscription = subscriptionOptional.get();

        if (subscription.getSharedWith() != null) {
            System.out.println("The subscription has already been shared.");
            return ResponseEntity.badRequest().body("The subscription has already been shared.");
        }

        User shareWithUser = userService.findByUsername(shareWithUsername);
        if (shareWithUser == null) {
            System.out.println("The user you want to share with was not found.");
            return ResponseEntity.badRequest().body("The user you want to share with was not found.");
        }

        // ----------- price calculation -------------

        Double sharedPrice = subscription.getPrice() / 2;
        Integer sharedRemainingContent = subscription.getRemainingContent() / 2;

        Subscription sharedSubscription = new Subscription(shareWithUser, subscription.getCategory(),
                sharedRemainingContent, sharedPrice, subscription.getStartDate());

        sharedSubscription.setStartPaymentDate(subscription.getStartPaymentDate());
        sharedSubscription.setSharedWith(shareWithUser);

        subscription.setSharedWith(shareWithUser);
        subscription.setPrice(sharedPrice);
        subscription.setRemainingContent(sharedRemainingContent);

        subscriptionService.addSubscription(sharedSubscription);
        subscriptionService.updateSubscription(subscription);

        System.out.println("Subscription has been successfully shared.");
        return ResponseEntity.ok("Subscription has been successfully shared.");
    }

}