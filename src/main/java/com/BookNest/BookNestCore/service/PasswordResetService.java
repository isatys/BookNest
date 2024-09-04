package com.BookNest.BookNestCore.service;

public interface PasswordResetService {
    void createPasswordResetTokenForUser(String email);
}
