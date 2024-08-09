package com.BookNest.BookNestCore.service;

import com.BookNest.BookNestCore.model.User;

public interface UserService {

    User findByUsername(String username);

    void saveUser(User user);

    void addAdminUser(String username,String password);
}
