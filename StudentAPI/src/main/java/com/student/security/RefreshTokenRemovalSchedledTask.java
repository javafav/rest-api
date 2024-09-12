package com.student.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.student.repository.RefreshTokenRepository;

import jakarta.transaction.Transactional;

@Component
@EnableScheduling
@Transactional
public class RefreshTokenRemovalSchedledTask {

	private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokenRemovalSchedledTask.class);
	@Autowired private RefreshTokenRepository repo;
	
	@Scheduled(fixedDelayString = "${app.refresh-token.removal.intervel}", initialDelay = 5000)
	public void deleteExpiredToken() {
		int deleteByExpiryTime = repo.deleteByExpiryTime();
		
		LOGGER.info("Expired Token Deleted: " + deleteByExpiryTime);
	}
}
