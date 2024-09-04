package com.BookNest.BookNestCore;

import com.BookNest.BookNestCore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//Configuration de latche pour envoyer un email quand l'emprunt sera fini à une semaine de la date de fin
@EnableScheduling
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
