package com.example.spring_security_web_service;

import com.example.spring_security_web_service.Security.Secret.Secret;
import com.example.spring_security_web_service.User.Gender;
import com.example.spring_security_web_service.User.Role.Role;
import com.example.spring_security_web_service.User.Role.UserRole;
import com.example.spring_security_web_service.User.User;
import com.example.spring_security_web_service.User.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class spring_security_web_service implements CommandLineRunner {
	private static final Logger log = LoggerFactory.getLogger(spring_security_web_service.class);

	public static void main(String[] args) {
		SpringApplication.run(spring_security_web_service.class, args);
		log.info("|<----------------| Your Spring Application has started......! |---------------->|");
	}

	@Override
	public void run(String ... args) {
		for(String s : args)
		{
			log.info(s);
		}
	}
	@Bean
	@Autowired
	CommandLineRunner commandLineRunner(
			UserService userService
	)
	{
		return (args) -> {
//			User john = null;
//			User johnDoe = null;
			try {
//				log.info(userService.deleteByUsername("john@gmail.com").toString());
//				log.info(userService.deleteByUsername("jane@gmail.com").toString());
				Role ADMIN = new Role(UserRole.ADMIN, "Administrator");
				Role USER = new Role(UserRole.USER, "User");
				User john = new User(
						"John Doe",
						"john@gmail.com",
						new Secret("password"),
						Gender.MALE,
						LocalDate.of(2001, Month.APRIL, 15),
						"Professor",
						Set.of(
								 ADMIN
						)
				);
				User jane = new User(
						"Jane Doe",
						"jane@gmail.com",
						new Secret("password"),
						Gender.FEMALE,
						LocalDate.of(2002, Month.APRIL, 19),
						"Professor",
						Set.of(
								USER
						)
				);
					log.info("\n{}",userService.saveAll(List.of(john,jane)).toString());
			} catch (Exception e){
				log.info(e.getLocalizedMessage());
				e.printStackTrace();

			}

		};
	}

}
