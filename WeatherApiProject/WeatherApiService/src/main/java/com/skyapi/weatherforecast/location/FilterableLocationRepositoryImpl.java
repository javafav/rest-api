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

	@Autowired
	private EntityManager entityManager;

	@Override
	public Page<Location> listWithFilters(Pageable pageable, Map<String, Object> filterFields) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Location> entityQuery = builder.createQuery(Location.class);

		Root<Location> entityRoot = entityQuery.from(Location.class);
		
		Predicate[] predicates = createsPredicates(filterFields, builder, entityRoot);
		if(predicates.length > 0) {
			entityQuery.where(predicates);
		}

		// Sorting
		List<Order> listOrder = new ArrayList<>();
		pageable.getSort().stream().forEach(order -> {
			System.out.println("Sort Field: " + order.getProperty());
			listOrder.add(builder.asc(entityRoot.get(order.getProperty())));
		});

		entityQuery.orderBy(listOrder);

		TypedQuery<Location> typedQuery = entityManager.createQuery(entityQuery);

		typedQuery.setFirstResult((int) pageable.getOffset());
		typedQuery.setMaxResults(pageable.getPageSize());

		List<Location> resultList = typedQuery.getResultList();

		long totalRows = getTotalRows(filterFields);
		
		Page<Location> listLocations = new PageImpl<>(resultList, pageable, totalRows);

		return listLocations;
	}

	private Predicate[] createsPredicates(Map<String, Object> filterFields, CriteriaBuilder builder, Root<Location> root) {
		
		Predicate[] predicate = new Predicate[filterFields.size()];
		if (!filterFields.isEmpty()) {
		
			Iterator<String> iterator = filterFields.keySet().iterator();
			int i = 0;
			while (iterator.hasNext()) {
				String fieldName = iterator.next();
				Object filterValue = filterFields.get(fieldName);
				predicate[i++] = builder.equal(root.get(fieldName), filterValue);

			}
			
		
		}
		return predicate;
	}
	
	
	private long getTotalRows(Map<String, Object > filterFields) {
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		
		Root<Location> countRoot = countQuery.from(Location.class);
		
		countQuery.select(builder.count(countRoot));
		
		Predicate[] predicates = createsPredicates(filterFields, builder, countRoot);
		
		if(predicates.length > 0) countQuery.where(predicates);
		
		Long rowsCount = entityManager.createQuery(countQuery).getSingleResult();
		
		return rowsCount;
		
		
	}

}
