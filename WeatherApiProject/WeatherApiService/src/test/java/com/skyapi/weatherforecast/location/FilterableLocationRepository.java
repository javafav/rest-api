package com.skyapi.weatherforecast.location;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.skyapi.weatherforecast.common.Location;

public interface FilterableLocationRepository {

	public Page<Location> listWithFilters(Pageable pageable, Map<String, Object> filterFields);
}
