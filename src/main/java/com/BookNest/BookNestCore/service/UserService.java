package com.BookNest.BookNestCore.service;

import com.BookNest.BookNestCore.model.User;
import com.BookNest.BookNestCore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

public interface UserService {

    User findByUsername(String username);

    void saveUser(User user);
}
