package com.skyapi.weatherforecast.location;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.skyapi.weatherforecast.common.Location;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Repository
public class FilterableLocationRepositoryImpl implements FilterableLocationRepository {

	@Autowired private EntityManager entityManager;
	
	
	@Override
	public Page<Location> listWithFilters(Pageable pageable, Map<String, Object> filterFields) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Location> query = builder.createQuery(Location.class);
		
		Root<Location> from = query.from(Location.class);
		
		TypedQuery<Location> typedQuery = entityManager.createQuery(query);
		
		List<Location> resultList = typedQuery.getResultList();
		
		int totalRows = 0;
		Page<Location> listLocations = new PageImpl<>(resultList, pageable, totalRows);
		
		return listLocations;
	}

}
