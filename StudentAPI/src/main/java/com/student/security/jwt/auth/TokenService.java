package com.student.security.jwt.auth;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	private PasswordEncoder passwordEncoder;

	public AuthResponse generateTokens(User user) {
		String token = jwtUtil.generateAccessToken(user);

		AuthResponse response = new AuthResponse();

		response.setAccessToken(token);

		String randomUUID = UUID.randomUUID().toString();

		response.setRefreshToken(randomUUID);

		long refreshTokenExpirationInMillis = System.currentTimeMillis() + refreshTokenExpiration * 60000;

		RefreshToken refreshToken = new RefreshToken();

		refreshToken.setToken(passwordEncoder.encode(randomUUID));
		refreshToken.setExpiryTime(new Date(refreshTokenExpirationInMillis));
		refreshToken.setUser(user);

		refreshTokenRepo.save(refreshToken);

		return response;
	}
	
	public AuthResponse refreshTokens(RefreshTokenRequest request) throws RefreshTokensNotFoundException, RefreshTokensExpiredException {
		String rawRefreshToken = request.getRefreshToken();
		
		List<RefreshToken> listRefreshTokens = refreshTokenRepo.findByUsername(request.getUsername());
		
		RefreshToken foundRefreshToken = null;
		
		for (RefreshToken token : listRefreshTokens) {
			if (passwordEncoder.matches(rawRefreshToken, token.getToken())) {
				foundRefreshToken = token;
			}
		}
		
		if (foundRefreshToken == null)
			throw new RefreshTokensNotFoundException();
		
		Date currentTime = new Date();
		
		if (foundRefreshToken.getExpiryTime().before(currentTime))
			throw new RefreshTokensExpiredException();
		
		AuthResponse response = generateTokens(foundRefreshToken.getUser());
		
		refreshTokenRepo.delete(foundRefreshToken);
		
		return response;
	}

}
