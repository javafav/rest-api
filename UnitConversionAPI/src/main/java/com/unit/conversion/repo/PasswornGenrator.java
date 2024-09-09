package com.unit.conversion.repo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class PasswornGenrator {

	public static void main(String[] args) {
		String password = "cup";
		String encodedPassword = new BCryptPasswordEncoder().encode(password);
		
		System.out.println("Password: " + encodedPassword);
	}

}
