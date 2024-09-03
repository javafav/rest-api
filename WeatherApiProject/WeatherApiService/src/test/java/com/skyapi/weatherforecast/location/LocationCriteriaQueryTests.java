package com.skyapi.weatherforecast.location;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.skyapi.weatherforecast.common.Location;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class LocationCriteriaQueryTests {

	@Autowired
	private EntityManager entityManager;
	
	@Test
	public void testCriteriaQuery() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Location> query = builder.createQuery(Location.class);
		
		Root<Location> root = query.from(Location.class);

		
		//Add WHERE clause
		Predicate predicate = builder.equal(root.get("countryCode"), "PK");
		query.where(predicate);
		
		//Add ORDER BY clause
		Order orderByCityName = builder.asc(root.get("cityName") );
		query.orderBy(orderByCityName);
		
	
	
		
		TypedQuery<Location> typedQuery = entityManager.createQuery(query);
		
		typedQuery.setFirstResult(0);
		typedQuery.setMaxResults(3);
		
		List<Location> listLocation = typedQuery.getResultList();
		
		
		assertThat(listLocation).isNotEmpty();
		
		listLocation.forEach(System.out::println);
		
		
	}
	
	@Test
	public void testJPQLQuery() {
		String jpql ="FROM Location WHERE countryCode = 'PK' ORDER BY 'cityName'";
		
	TypedQuery<Location> typedQuery = entityManager.createQuery(jpql, Location.class);
		
		List<Location> listLocation = typedQuery.getResultList();
		
		assertThat(listLocation).isNotEmpty();
		
		listLocation.forEach(System.out::println);
		
	}
}
