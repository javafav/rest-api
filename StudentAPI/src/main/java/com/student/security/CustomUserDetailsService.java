package com.student.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.student.repository.User;
import com.student.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired private UserRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		Optional<User> findByUsername = repo.findByUsername(username);
		if(findByUsername == null) {
			throw new UsernameNotFoundException("No user found with given username: " + username);
		}
		
		return new CustomUserDetails(findByUsername.get());
	}

}
