package com.student.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RefreshTokenRepositroyTests {

	@Autowired RefreshTokenRepository repo;
	@Autowired TestEntityManager testEntityManager;
	
	@Test
	public void testFindByUsernameNotFound() {
		String usernameNotExist = "xyzab";
		
		List<RefreshToken> listRefreshToken = repo.findByUsername(usernameNotExist);
		
		assertThat(listRefreshToken).isEmpty();
	}
	
	@Test
	public void testFindByUsernameFound() {
		String usernameExist = "admin";
		
		List<RefreshToken> listRefreshTokens = repo.findByUsername(usernameExist);
		
		assertThat(listRefreshTokens).isNotEmpty();
	}
	  @Test
	    public void testDeleteByExpiryTime() {
	      
	        String jpql = "SELECT COUNT(rt) FROM RefreshToken rt WHERE rt.expiryTime <= CURRENT_TIME";
	        Query query = testEntityManager.getEntityManager().createQuery(jpql);
	        Long numberOfExpiredToken =(Long) query.getSingleResult();
	        

	     
	        int rowDeleted = repo.deleteByExpiryTime();

	        assertThat(numberOfExpiredToken).isEqualTo(rowDeleted);
	    }
}
