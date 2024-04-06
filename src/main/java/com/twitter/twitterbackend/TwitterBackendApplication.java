package com.twitter.twitterbackend;

import com.twitter.twitterbackend.models.ApplicationUser;
import com.twitter.twitterbackend.models.Role;
import com.twitter.twitterbackend.repositories.RoleRepository;
import com.twitter.twitterbackend.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class TwitterBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwitterBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepository, UserService userService){
		return args -> {
			roleRepository.save(new Role(1, "USER"));
//			ApplicationUser u = new ApplicationUser();
//			u.setFirstName("Unknown");
//			u.setLastName("Koder");
//
//			userService.registerUser(u);
		};
	}
}
