package com.example.sogeti.controller;

import com.example.sogeti.model.Category;
import com.example.sogeti.model.User;
import com.example.sogeti.repos.CategoryRepo;
import com.example.sogeti.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


@Controller
public class MainController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CategoryRepo categoryRepo;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }
    @GetMapping("/main")
    public String main(Model model) {
        Iterable<Category> categories = categoryRepo.findAll();
        model.addAttribute("categories", categories);
        return "main";
    }

    @PostMapping("/main")
    public String add(
            @RequestParam String name,
            @RequestParam Integer availableContent,
            @RequestParam Double price,
            Model model) {

        Category category = new Category(name, availableContent, price);
        categoryRepo.save(category);
        Iterable<Category> categories = categoryRepo.findAll();
        model.addAttribute("categories", categories);
        return "main";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        List<User> users = userRepo.findAll();          // getting all users
        model.addAttribute("users", users); // adding a list of users to the model
        return "admin";                                 // returns "admin.mustache"
    }

    @GetMapping("/categories")
    public String categories(Model model) {
        Iterable<Category> categories = categoryRepo.findAll();
        model.addAttribute("categories", categories);
        return "categories";
    }
}