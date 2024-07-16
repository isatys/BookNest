package com.BookNest.BookNestCore;

import com.BookNest.BookNestCore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookNestCoreApplication {
	@Autowired
	private UserService userService;
	public static void main(String[] args) {
		SpringApplication.run(BookNestCoreApplication.class, args);
	}
	public void run(String... args) throws Exception {
		userService.addAdminUser("isatys", "riviere");
	}
}
