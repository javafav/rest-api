package com.unit.conversion.rep;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswornGenrator implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		String password = "khan";
		String encodedPassword = new BCryptPasswordEncoder().encode(password);;
		System.out.println("Password: " +encodedPassword);
		

	}

}
