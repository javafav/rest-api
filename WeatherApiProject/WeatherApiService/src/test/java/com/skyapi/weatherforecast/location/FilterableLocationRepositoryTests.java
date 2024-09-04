package com.skyapi.weatherforecast.location;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;
import com.skyapi.weatherforecast.common.Location;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class FilterableLocationRepositoryTests {

	
	@Autowired
	private LocationRepository repo;
	
	@Test
	public void testListWithDefaults() {
		int pageNum = 1;
		int pageSize = 5;
		
		String sortField = "code";
		Sort sort = Sort.by(sortField).ascending();
		
		Pageable pageable = PageRequest.of(pageNum -1, pageSize, sort);
		Page<Location> page = repo.listWithFilters(pageable, Collections.emptyMap());
		List<Location> content = page.getContent();
		content.forEach(System.out::println);

        System.out.println("Total Elemetns: " + page.getTotalElements());
		assertThat(page.getTotalElements()).isGreaterThan(pageable.getOffset() + content.size());
		assertThat(content.size()).isEqualTo(pageSize);
		assertThat(content).isSortedAccordingTo((new Comparator<Location>() {

			@Override
			public int compare(Location location1, Location location2) {
				return location1.getCode().compareTo(location2.getCode());
			}
		}));
		
	}
	
	@Test
	public void testListNoFiltersSortedByCityName() {
		int pageNum = 1;
		int pageSize = 5;
		
		String sortField = "cityName";
		Sort sort = Sort.by(sortField).ascending();
		
		Pageable pageable = PageRequest.of(pageNum -1, pageSize, sort);
		Page<Location> page = repo.listWithFilters(pageable, Collections.emptyMap());
		List<Location> content = page.getContent();
		content.forEach(System.out::println);

		System.out.println("Total Elemetns: " + page.getTotalElements());
		assertThat(page.getTotalElements()).isGreaterThan(pageable.getOffset() + content.size());
		assertThat(content.size()).isEqualTo(pageSize);
		assertThat(content).isSortedAccordingTo((new Comparator<Location>() {

			@Override
			public int compare(Location location1, Location location2) {
				return location1.getCityName().compareTo(location2.getCityName());
			}
		}));
		
	}
	
	@Test
	public void testListFilterByRegionNameSortedByCityName() {
		int pageNum = 1;
		int pageSize = 5;
		
		String sortField = "cityName";
		String regionName = "England";
		Sort sort = Sort.by(sortField).ascending();
		
		Map<String, Object> filterFields = new HashMap<>();
		filterFields.put("regionName", regionName);
		
		Pageable pageable = PageRequest.of(pageNum -1, pageSize, sort);
		Page<Location> page = repo.listWithFilters(pageable, filterFields);
		List<Location> content = page.getContent();
		content.forEach(System.out::println);

		System.out.println("Total Elemetns: " + page.getTotalElements());
		assertThat(page.getTotalElements()).isGreaterThan(pageable.getOffset() + content.size());
		assertThat(content.size()).isEqualTo(pageSize);
		assertThat(content).isSortedAccordingTo((new Comparator<Location>() {

			@Override
			public int compare(Location location1, Location location2) {
				return location1.getCityName().compareTo(location2.getCityName());
			}
		}));
		content.forEach(con -> assertThat(con.getRegionName()).isEqualTo(regionName));
		
	}
	
	@Test
	public void testListFilterByCountryCodeSortedByCode() {
		int pageNum = 2;
		int pageSize = 5;
		
		String sortField = "code";
		String countryCode = "US";
		Sort sort = Sort.by(sortField).ascending();
		
		Map<String, Object> filterFields = new HashMap<>();
		filterFields.put("countryCode", countryCode);
		
		Pageable pageable = PageRequest.of(pageNum -1, pageSize, sort);
		Page<Location> page = repo.listWithFilters(pageable, filterFields);
		List<Location> content = page.getContent();
		

		System.out.println("Total Elemetns: " + page.getTotalElements());
		assertThat(page.getTotalElements()).isGreaterThan(pageable.getOffset() + content.size());
		
		assertThat(content.size()).isEqualTo(pageSize);
		assertThat(content).isSortedAccordingTo((new Comparator<Location>() {

			@Override
			public int compare(Location location1, Location location2) {
				return location1.getCode().compareTo(location2.getCode());
			}
		}));
		content.forEach(con -> assertThat(con.getCountryCode()).isEqualTo(countryCode));
		content.forEach(System.out::println);
		
	}
	
	@Test
	public void testListFilterByCountryCodeAndEnabeldAndSortedByCityName() {
		int pageNum = 1;
		int pageSize = 5;
		
		String sortField = "code";
		String countryCode = "US";
		boolean enabled = true;
		Sort sort = Sort.by(sortField).ascending();
		
		Map<String, Object> filterFields = new HashMap<>();
		filterFields.put("countryCode", countryCode);
		filterFields.put("enabled", enabled);
		
		Pageable pageable = PageRequest.of(pageNum -1, pageSize, sort);
		Page<Location> page = repo.listWithFilters(pageable, filterFields);
		List<Location> content = page.getContent();
		

		System.out.println("Total Elemetns: " + page.getTotalElements());
		assertThat(page.getTotalElements()).isGreaterThan(pageable.getOffset() + content.size());
		assertThat(content.size()).isEqualTo(pageSize);
		assertThat(content).isSortedAccordingTo((new Comparator<Location>() {

			@Override
			public int compare(Location location1, Location location2) {
				return location1.getCode().compareTo(location2.getCode());
			}
		}));
		content.forEach(con -> {
		assertThat(con.isEnabled()).isEqualTo(enabled);
		assertThat(con.getCountryCode()).isEqualTo(countryCode);
		});
		
		content.forEach(System.out::println);
		
	}




}
