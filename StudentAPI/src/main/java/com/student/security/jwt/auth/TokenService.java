package com.student.security.jwt.auth;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.RsaAlgorithm;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.student.repository.RefreshToken;
import com.student.repository.RefreshTokenRepository;
import com.student.repository.User;
import com.student.security.jwt.JwtUtility;

@Service
public class TokenService {

	@Value("${app.security.jwt.refresh-token-expiration}")
	private int refreshTokenExpiration;

	@Autowired
	private JwtUtility jwtUtil;

	@Autowired
	private RefreshTokenRepository refreshTokenRepo;

	@Autowired
	private PasswordEncoder encoder;

	public AuthResponse generateTokens(User user) {
		String token = jwtUtil.generateAccessToken(user);

		AuthResponse response = new AuthResponse();

		response.setAccessToken(token);

		String randomUUID = UUID.randomUUID().toString();

		response.setRefreshToken(randomUUID);

		long refreshTokenExpirationInMillis = System.currentTimeMillis() + refreshTokenExpiration * 60000;

		RefreshToken refreshToken = new RefreshToken();

		refreshToken.setToken(encoder.encode(randomUUID));
		refreshToken.setExpiryTime(new Date(refreshTokenExpirationInMillis));
		refreshToken.setUser(user);

		refreshTokenRepo.save(refreshToken);

		return response;
	}

}
