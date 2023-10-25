package com.example.sogeti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

//    http://localhost:8080/login
    @GetMapping("/login")
    public String login(@RequestParam(name = "error", required = false) String error, Model model) {

        System.out.println("error = " + error);
        if (error != null) {
            model.addAttribute("errorMessage", "Wrong user name or password"); // Сообщение об ошибке
            System.out.println("Wrong user name or password");
        }else{
            System.out.println("login Success");
            model.addAttribute("loginSuccessMessage", "login Success");
        }
        return "login";
    }
}
