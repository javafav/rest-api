package com.student.security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.student.repository.User;
import com.student.security.CustomUserDetails;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	private Logger LOGGER = LoggerFactory.getLogger(JwtFilter.class);
	
	@Autowired private JwtUtility jwtUtil;
	
	@Qualifier("handlerExceptionResolver")
	@Autowired HandlerExceptionResolver exceptionResolver;
	

	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(!hasAuthorizationBearer(request)) {
			filterChain.doFilter(request, response);
			return ;
		}
		
		String token = getBearerToken(request);
		
		LOGGER.info("Token: " + token);
		
		try {
			Claims claims = jwtUtil.validateAccessToken(token);
			
			UserDetails userDetails = getUserDetails(claims);
			
			setAuthenticationContext(userDetails, request);
			
			filterChain.doFilter(request, response);
			
			clearAuthenticationContext();
	
		}  catch (JwtValidationException ex) {
			LOGGER.error(ex.getMessage(), ex);
			
			exceptionResolver.resolveException(request, response, null, ex);
		}

	}


	private void clearAuthenticationContext() {
		SecurityContextHolder.clearContext();
		
	}


	private void setAuthenticationContext(UserDetails userDetails, HttpServletRequest request) {
		var authentication = new UsernamePasswordAuthenticationToken(
				userDetails, null, userDetails.getAuthorities());
		
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}


	private UserDetails getUserDetails(Claims claims) {
		String subject = (String) claims.get(Claims.SUBJECT);

		String[] array = subject.split(",");
		
		Integer userId = Integer.valueOf(array[0]);
		String username  = array[1];
		
		User user = new User();
		user.setId(userId);
		user.setUsername(username);
		
		String role = (String) claims.get("role");
		user.setRole(role);
		
		LOGGER.info("The Parsed User from JWT ID: " + user.getId() + " User name: " +  user.getUsername() + " Role: " + user.getRole());
		
		return new CustomUserDetails(user);
		
	}


	private boolean hasAuthorizationBearer(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		
	
		LOGGER.info("Logger Authorization: " + header);
		
		if(ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) {
			return false;
		}
		
		
		 return true;
	}
	
	public String getBearerToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		
		String[] array = header.split(" ");
		
		if(array.length == 2) {
			return array[1];
		}
		return null;
	}

}
