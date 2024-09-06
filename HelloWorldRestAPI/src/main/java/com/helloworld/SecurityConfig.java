package com.helloworld;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	
	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails user1 = User.withUsername("admin").password("{noop}nimda").build();
		UserDetails user2 = User.withUsername("john").password("{noop}johny").build();
		UserDetails user3 = User.withUsername("tim").password("{noop}timmy").build();
		
		return new InMemoryUserDetailsManager(user1, user2, user3);
	}
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
				
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				
				.httpBasic(Customizer.withDefaults())
				   .build();
	}

}
