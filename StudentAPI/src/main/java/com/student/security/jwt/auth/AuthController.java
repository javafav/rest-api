package com.student.security.jwt.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.student.security.CustomUserDetails;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/oauth")

public class AuthController {

	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private TokenService tokenService;

	@PostMapping("/token")
	public ResponseEntity<?> getAccessToekn(@RequestBody @Valid AuthRequest request) {
		String username = request.getUsername();
		String password = request.getPassword();
		
		try {
			Authentication authentication = authManager
					                   .authenticate(new UsernamePasswordAuthenticationToken(username, password));
			
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			
			AuthResponse response = tokenService.generateTokens(userDetails.getUser());

			return ResponseEntity.ok(response);
		
		} catch (BadCredentialsException ex) {
			
			return ResponseEntity.badRequest().build();
		
		} catch (UsernameNotFoundException ex) {
		
			return ResponseEntity.notFound().build();
		}

	}
}
