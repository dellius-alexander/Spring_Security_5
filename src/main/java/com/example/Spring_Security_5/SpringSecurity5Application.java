package com.example.Spring_Security_5;

import com.example.Spring_Security_5.Security.Secret.Secret;
import com.example.Spring_Security_5.User.Gender;
import com.example.Spring_Security_5.User.Role.Role;
import com.example.Spring_Security_5.User.Role.UserRole;
import com.example.Spring_Security_5.User.User;
import com.example.Spring_Security_5.User.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class SpringSecurity5Application implements CommandLineRunner {
	private static final Logger log = LoggerFactory.getLogger(SpringSecurity5Application.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurity5Application.class, args);
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
				User johnDoe = userService.getByEmail("john@gmail.com");
				User janeDoe = userService.getByEmail("jane@gmail.com");
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
								 ADMIN,
								 USER
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
								ADMIN,
								USER
						)
				);

				if (johnDoe == null || janeDoe ==  null)
				{
					userService.saveAll(List.of(john,jane));
					log.info(john.toString());
					log.info(jane.toString());
				}
				else
				{
					log.info(johnDoe.toString());
					log.info(janeDoe.toString());
				}
			} catch (Exception e){
				log.info(e.getLocalizedMessage());
				e.printStackTrace();

			}

		};
	}

}
