package com.example.sogeti.controller;

import com.example.sogeti.model.User;
import com.example.sogeti.repos.UserRepo;
import com.example.sogeti.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/subscriptionsView")
public class SubscriptionViewController {

    private final UserRepo userRepo;
    private final CategoryService categoryService;

    // inject into the constructor
    @Autowired
    public SubscriptionViewController(UserRepo userRepo, CategoryService categoryService) {
        this.userRepo = userRepo;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getSubscriptionsView(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();

        Map<String, Object> categoriesData = categoryService.getCategoriesData(username);

        model.addAttribute("availableCategories", categoriesData.get("availableCategories"));
        model.addAttribute("subscribedCategories", categoriesData.get("subscribedCategories"));

        System.out.println(categoriesData.get("subscribedCategories"));

        List<User> users = userRepo.findAll();
        model.addAttribute("users", users);

        return "subscriptionsView";
    }
}
