package com.skyapi.weatherforecast.location;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.daily.DailyWeatherApiController;
import com.skyapi.weatherforecast.hourly.BadRequestException;
import com.skyapi.weatherforecast.hourly.HourlyWeatherApiController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/v1/locations")
@Validated
public class LocationApiController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HourlyWeatherApiController.class);

	private LocationService service;
	private ModelMapper modelMapper;

	private Map<String, String> fieldMapper = Map.of("code", "code", "city_name", "cityName", "region_name",
			"regionName", "country_code", "countryCode", "country_name", "countryName", "enabled", "enabled");

	public LocationApiController(LocationService service, ModelMapper modelMapper) {
		super();
		this.service = service;
		this.modelMapper = modelMapper;
	}

	@PostMapping
	public ResponseEntity<LocationDTO> add(@RequestBody @Valid LocationDTO dto) {

		Location addedLocation = service.add(dto2Entity(dto));
		URI uri = URI.create("/v1/locations/" + addedLocation.getCode());

		return ResponseEntity.created(uri).body(entiy2DTO(addedLocation));
	}

	@Deprecated
	public ResponseEntity<?> listLocations() {
		List<Location> locations = service.list();

		if (locations.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(listEntity2ListDTO(locations));
	}

	@GetMapping
	public ResponseEntity<?> listLocations(
			@RequestParam(value = "page", defaultValue = "1", required = false) @Min(value = 1) Integer pageNum,
			@RequestParam(value = "size", defaultValue = "2", required = false) @Min(value = 2) @Max(value = 20) Integer pageSize,
			@RequestParam(value = "sort", defaultValue = "code", required = false) String sortField)
			throws BadRequestException {

		if (!fieldMapper.containsKey(sortField)) {
			throw new BadRequestException("Invalid sort field: " + sortField);
		}
		Page<Location> page = service.listByPage(pageNum - 1, pageSize, fieldMapper.get(sortField));
		List<Location> locations = page.getContent();

		if (locations.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(addPageMetaDataAndLinks(listEntity2ListDTO(locations), page, sortField));

	}

	private CollectionModel<LocationDTO> addPageMetaDataAndLinks(List<LocationDTO> listDTO, Page<Location> pageInfo,
			String sortField) throws BadRequestException {
		// Adding self link to individual location DTO
		for (LocationDTO dto : listDTO) {

			dto.add(linkTo(methodOn(LocationApiController.class).getLocation(dto.getCode())).withSelfRel());
		}

		int pageSize = pageInfo.getSize();
		long totalElements = pageInfo.getTotalElements();
		int pageNum = pageInfo.getNumber() + 1;
		int totalPages = pageInfo.getTotalPages();

		PageMetadata pageMetadata = new PageMetadata(pageSize, pageNum, totalElements);

		CollectionModel<LocationDTO> collectionModel = PagedModel.of(listDTO, pageMetadata);
		
		collectionModel.add(linkTo(methodOn(LocationApiController.class).listLocations(pageNum, pageSize, sortField))
				.withSelfRel());
		
		if( pageNum > 1) {
			//Adding first page link if current page is not the first page
			collectionModel.add(linkTo(methodOn(LocationApiController.class).listLocations(1, pageSize, sortField))
					.withRel(IanaLinkRelations.FIRST));
			
			//Adding previous page link if current page is not the first page
			collectionModel.add(linkTo(methodOn(LocationApiController.class).listLocations(pageNum - 1, pageSize, sortField))
					.withRel(IanaLinkRelations.PREV));
		}
		
		if(pageNum < totalPages) {
			//Adding next page link if current page is not the last page
			collectionModel.add(linkTo(methodOn(LocationApiController.class).listLocations(pageNum + 1, pageSize, sortField))
					.withRel(IanaLinkRelations.NEXT));
			
			collectionModel.add(linkTo(methodOn(LocationApiController.class).listLocations(totalPages, pageSize, sortField))
					.withRel(IanaLinkRelations.LAST));
		
		}
		
		return collectionModel;
	}

	@GetMapping("/{code}")
	public ResponseEntity<?> getLocation(@PathVariable("code") String code) {
		Location location = service.get(code);

		return ResponseEntity.ok(entiy2DTO(location));
	}

	@PutMapping
	public ResponseEntity<?> updateLocation(@RequestBody @Valid LocationDTO dto) {

		Location updatedLocation = service.update(dto2Entity(dto));
		return ResponseEntity.ok(entiy2DTO(updatedLocation));

	}

	@DeleteMapping("/{code}")
	public ResponseEntity<?> deleteLocation(@PathVariable("code") String code) {

		service.delete(code);
		return ResponseEntity.noContent().build();

	}

	private List<LocationDTO> listEntity2ListDTO(List<Location> locations) {

		List<LocationDTO> lstDTO = new ArrayList<>();

		locations.forEach(location -> {
			lstDTO.add(modelMapper.map(location, LocationDTO.class));
		});

		return lstDTO;

	}

	public LocationDTO entiy2DTO(Location location) {
		return modelMapper.map(location, LocationDTO.class);
	}

	public Location dto2Entity(LocationDTO dto) {
		return modelMapper.map(dto, Location.class);
	}
}
