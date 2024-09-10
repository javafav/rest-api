package com.student.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTests {

	@Autowired private UserRepository repo;
	
	PasswordEncoder encoder = new BCryptPasswordEncoder();
	
	@Test
	public void testAddNewUser() {
		User user = new User();
		user.setUsername("sany");
		user.setRole("read");
		
		String rawPassword = "ynas";
		String encodedPassword = encoder.encode(rawPassword);
		user.setPassword(encodedPassword);
		
		User savedUser = repo.save(user);
		
		assertThat(savedUser).isNotNull();
		
	}
	
	@Test
	public void testFindUserNotFound() {
		Optional<User> user = repo.findByUsername("xyz");
		
		assertThat(user).isNotPresent();
	}
	
	@Test
	public void testFindUseFound() {
		String userName = "sany";
		Optional<User> optionalUser = repo.findByUsername(userName);
		User user = optionalUser.get();
		
		assertThat(optionalUser).isPresent();
		
		assertThat(user.getUsername()).isEqualTo(userName);
	
	}
}
