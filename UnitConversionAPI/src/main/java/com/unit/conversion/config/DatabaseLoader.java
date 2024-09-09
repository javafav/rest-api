package com.unit.conversion.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.unit.conversion.repo.UserRepository;
import com.unit.conversion.user.User;

@Configuration
public class DatabaseLoader implements CommandLineRunner {

	@Autowired private UserRepository repo;
	@Override
	public void run(String... args) throws Exception {
	
		User user1 = new User ("sany","$2a$10$x.WHXs1mJwk/msHghaxNVepItSqJec6dERw8URweWetivzP6hsGtS");//password : khan
		User user2 = new User ("ali","$2a$10$W7G7PP7QK.GU/xZ692Kvl.LaEFoK29Xx3br82x7WS/2gYKA6ScSKq");//password : test
		User user3 = new User ("rauf","$2a$10$BIh0UvF6OXaW06wTm1lXruLQ8Z.SlxipF0UnS1BvNlgbkfWKPbn..");//password : test
		
		
		repo.saveAll(List.of(user1, user2, user3));
		
		System.out.println("All user saved");
		
		

	}

}
