package com.BookNest.BookNestCore.service.impl;

import com.BookNest.BookNestCore.model.User;
import com.BookNest.BookNestCore.repository.UserRepository;
import com.BookNest.BookNestCore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveUser(User user) {
        userRepository.save(user);
    }


    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            System.out.println("User found: " + user.getUsername());
        } else {
            System.out.println("User not found with username: " + username);
        }
        return user;
    }
}
