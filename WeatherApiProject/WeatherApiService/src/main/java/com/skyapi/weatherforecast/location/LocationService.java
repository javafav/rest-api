package com.skyapi.weatherforecast.location;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.AbstractLocationService;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.hourly.BadRequestException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LocationService extends AbstractLocationService {

	public LocationService(LocationRepository repo) {
		super();
		this.repo = repo;
	}

	public Location add(Location location) {
		return repo.save(location);
	}

	@Deprecated
	public List<Location> list() {
		return repo.findUntrashed();
	}

	@Deprecated
	public Page<Location> listByPage(int pageNum, int pageSize, String sortField) throws BadRequestException {
		Sort sort = Sort.by(sortField).ascending();
		Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

		return repo.findUntrashed(pageable);

	}

	public Page<Location> listByPage(int pageNum, int pageSize, String sortOption, Map<String, Object> filterFields)
			throws BadRequestException {
		String[] sortFields = sortOption.split(",");
		Sort sort = null;

		if (sortFields.length > 1) { // sort using multiple fields

			String firstFieldName = sortFields[0];
			String actulFirstFieldName = sortOption.replace("-", "");

			sort = firstFieldName.startsWith("-") ? Sort.by(actulFirstFieldName).descending()
					: Sort.by(actulFirstFieldName).ascending();

			for (int i = 1; i < sortFields.length; i++) {
				String nextFieldName = sortFields[i];
				String actualNextFieldName = nextFieldName.replace("-", "");

				sort = sort.and(nextFieldName.startsWith("-") ? 
						Sort.by(actualNextFieldName).descending() : Sort.by(actualNextFieldName).ascending());
			}

		} else {// sort using single field
			String actulFields = sortOption.replace("-", "");
			sort = sortOption.startsWith("-") ? Sort.by(actulFields).descending() : Sort.by(actulFields).ascending();
		}

		Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

		return repo.listWithFilters(pageable, filterFields);

	}

	public Location update(Location locationInRequest) {
		String code = locationInRequest.getCode();
		Location locationInDb = repo.findByCode(code);

		if (locationInDb == null) {
			throw new LocationNotFoundException(code);
		}

		locationInDb.copyFieldsFrom(locationInRequest);

		Location updatedLocation = repo.save(locationInDb);
		return updatedLocation;
	}

	public void delete(String code) {

		Location locationInDb = repo.findByCode(code);

		if (locationInDb == null) {
			throw new LocationNotFoundException(code);
		}

		repo.trashedByCode(code);
	}
}
