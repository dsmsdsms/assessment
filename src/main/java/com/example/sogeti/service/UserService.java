package com.example.sogeti.service;

import com.example.sogeti.model.User;

public interface UserService {
    User findByUsername(String username);
    void save(User user);

}
