package com.student.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

import com.student.security.jwt.JwtFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

	@Autowired JwtFilter jwtFilter;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
		return authConfiguration.getAuthenticationManager();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(
				auth -> auth.requestMatchers("/api/oauth/**").permitAll()
			             	.requestMatchers(HttpMethod.GET, "/api/students").hasAnyAuthority("read", "write")
			             	.requestMatchers(HttpMethod.POST, "/api/students").hasAuthority("write")
			             	.requestMatchers(HttpMethod.PUT, "/api/students").hasAuthority("write")
			             	.anyRequest().authenticated())
		
		.csrf(csrf -> csrf.disable())

				.exceptionHandling(exh -> exh.authenticationEntryPoint((request, response, exception) -> {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
				}))

				.addFilterBefore(jwtFilter, AuthorizationFilter.class);
				

		return http.build();
	}
}
