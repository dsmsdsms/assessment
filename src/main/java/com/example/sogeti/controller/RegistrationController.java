package com.example.sogeti.controller;

import com.example.sogeti.model.Role;
import com.example.sogeti.model.User;
import com.example.sogeti.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public ResponseEntity<Map<String, Object>> addUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();

        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            response.put("errorMessage", "User exists!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);
        response.put("successMessage", "User added successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/delete/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteUser(@PathVariable String username, Model model) {
        User userToDelete = userRepo.findByUsername(username);

        if (userToDelete == null) {
            System.out.println("Attempt to delete user: " + username + ". User not found!");
            model.addAttribute("errorMessage", "User not found!");
            return "account";
        }

        userRepo.delete(userToDelete);
        System.out.println("User: " + username + " was successfully deleted by admin.");
        return "redirect:/admin";
    }
}