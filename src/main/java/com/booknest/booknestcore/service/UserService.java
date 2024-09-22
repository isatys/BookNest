package com.booknest.booknestcore.service;

import com.booknest.booknestcore.model.User;

public interface UserService {

    User findByUsername(String username);

    User saveUser(User user);

    void addAdminUser(String username,String password);


    void sendEmail(String email, String demandeAcceptée, String s);
}
