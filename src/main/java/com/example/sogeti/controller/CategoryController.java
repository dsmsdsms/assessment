package com.example.sogeti.controller;

import com.example.sogeti.model.Category;
import com.example.sogeti.repos.CategoryRepo;
import com.example.sogeti.repos.SubscriptionRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryRepo categoryRepo;
    private final SubscriptionRepo subscriptionRepo;

    @Autowired
    public CategoryController(
            CategoryRepo categoryRepo,
            SubscriptionRepo subscriptionRepo
    ) {
        this.categoryRepo = categoryRepo;
        this.subscriptionRepo = subscriptionRepo;
    }

    protected static final Logger logger = LogManager.getLogger(CategoryController.class);

    // Endpoint
    // http://localhost:8080/category/user
    // GET
    @GetMapping("/currentUsername")
    public ResponseEntity<String> getCurrentUsername(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userDetails.getUsername());
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryRepo.findAll();
        return ResponseEntity.ok(categories);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        Optional<Category> existingCategory = categoryRepo.findByName(category.getName());
        if (existingCategory.isPresent()) {
            return ResponseEntity.badRequest().body("A category with the same name already exists");
        }
        categoryRepo.save(category);
        return ResponseEntity.ok("Category successfully created");
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<String> addCategory(@RequestBody Category category) {
        Optional<Category> existingCategory = categoryRepo.findByName(category.getName());
        if (existingCategory.isPresent()) {
            System.out.println("A category with the same name already exists: " + category.getName());
            return ResponseEntity.badRequest().body("A category with the same name already exists");
        }
        categoryRepo.save(category);
        System.out.println("Category successfully created: " + category.getName());
        return ResponseEntity.ok("Category successfully created");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id, @RequestHeader(name = "Accept") String acceptHeader) {
        if (!categoryRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        categoryRepo.deleteById(id);

        if ("text/plain".equals(acceptHeader)) {
            return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("Category successfully deleted");
        } else {
            return ResponseEntity.ok(Map.of("message", "Category successfully deleted"));
        }
    }
}
