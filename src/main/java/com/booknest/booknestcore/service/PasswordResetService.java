package com.booknest.booknestcore.service;

public interface PasswordResetService {
    void createPasswordResetTokenForUser(String email);
}
