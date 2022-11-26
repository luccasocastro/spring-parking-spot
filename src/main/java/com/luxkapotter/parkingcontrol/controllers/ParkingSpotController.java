package com.luxkapotter.parkingcontrol.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.luxkapotter.parkingcontrol.DTOS.ParkingSpotDTO;
import com.luxkapotter.parkingcontrol.models.ParkingSpot;
import com.luxkapotter.parkingcontrol.services.ParkingSpotService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/parking-spot")
public class ParkingSpotController {

	@Autowired
	private ParkingSpotService service;

	@PostMapping
	@ResponseBody
	public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDTO parkingSpotDto) {

		if (service.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plate Car is already in use!");
		}
		if (service.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is already in use!");
		}
		if (service.existsByApartmentAndBlock(parkingSpotDto.getApartment(), parkingSpotDto.getBlock())) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Conflict: Parking Spot already registered for this apartment/block!");
		}

		ParkingSpot parkingSpot = new ParkingSpot();
		BeanUtils.copyProperties(parkingSpotDto, parkingSpot);
		parkingSpot.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
		service.insert(parkingSpot);
		return new ResponseEntity<Object>(parkingSpot, HttpStatus.CREATED);
	}

	@GetMapping
	@ResponseBody
	public ResponseEntity<Page<ParkingSpot>> getAllParkingSpots(
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
		Page<ParkingSpot> list = service.findAll(pageable);
		return new ResponseEntity<Page<ParkingSpot>>(list, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@ResponseBody
	public ResponseEntity<Object> getOneParkingSpot(@PathVariable(value = "id") UUID id) {
		Optional<ParkingSpot> parkingSpotOptional = service.findById(id);
		if (!parkingSpotOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
		} else {
			parkingSpotOptional.get();
		}
		return new ResponseEntity<Object>(parkingSpotOptional, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity<String> deleteParkingSpot(@PathVariable(value = "id") UUID id) {
		Optional<ParkingSpot> parkingSpotOptional = service.findById(id);
		if (!parkingSpotOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
		}
		service.delete(parkingSpotOptional.get());
		return new ResponseEntity<String>("Deleted Successfully!", HttpStatus.NO_CONTENT);
	}

	@PutMapping("/{id}")
	@ResponseBody
	public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id") UUID id,
			@RequestBody @Valid ParkingSpotDTO parkingSpotDto) {
		Optional<ParkingSpot> parkingSpotOptional = service.findById(id);
		if (!parkingSpotOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
		}
		ParkingSpot obj = new ParkingSpot();
		obj = service.update(obj, parkingSpotDto, parkingSpotOptional);
		return new ResponseEntity<Object>(obj, HttpStatus.OK);
	}

}
