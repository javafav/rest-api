package com.skyapi.weatherforecast.location;

import java.util.ArrayList;
import java.util.Iterator;
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
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class FilterableLocationRepositoryImpl implements FilterableLocationRepository {

	@Autowired private EntityManager entityManager;
	
	
	@Override
	public Page<Location> listWithFilters(Pageable pageable, Map<String, Object> filterFields) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Location> query = builder.createQuery(Location.class);
		
		Root<Location> root = query.from(Location.class);
		if(!filterFields.isEmpty()) {
			Predicate[] predicate =new Predicate[filterFields.size()];
			Iterator<String> iterator = filterFields.keySet().iterator();
		    int i = 0;
			while(iterator.hasNext()) {
				String fieldName = iterator.next();
				Object filterValue = filterFields.get(fieldName);
				predicate[i++] = builder.equal(root.get(fieldName), filterValue);
				
			}
			query.where(predicate);
		}
		//Sorting 
		List<Order> listOrder = new ArrayList<>();
		pageable.getSort().stream().forEach(order -> {
			System.out.println("Sort Field: " +  order.getProperty());
		listOrder.add(builder.asc(root.get(order.getProperty())) );
		});
		
		query.orderBy(listOrder);
		
		
		TypedQuery<Location> typedQuery = entityManager.createQuery(query);
		
		
		
		typedQuery.setFirstResult(0);
		typedQuery.setMaxResults(pageable.getPageSize());
		
		List<Location> resultList = typedQuery.getResultList();
		
		int totalRows = 0;
		Page<Location> listLocations = new PageImpl<>(resultList, pageable, totalRows);
		
		return listLocations;
	}

}
