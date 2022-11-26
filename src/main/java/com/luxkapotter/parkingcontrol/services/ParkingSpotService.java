package com.luxkapotter.parkingcontrol.services;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.luxkapotter.parkingcontrol.DTOS.ParkingSpotDTO;
import com.luxkapotter.parkingcontrol.models.ParkingSpot;
import com.luxkapotter.parkingcontrol.repositories.ParkingSpotRepository;

@Service
public class ParkingSpotService {

	@Autowired
	private ParkingSpotRepository repository;

	@Transactional
	public ParkingSpot insert(ParkingSpot parkingSpot) {
		return repository.save(parkingSpot);
	}

	public boolean existsByLicensePlateCar(String licensePlateCar) {
		return repository.existsByLicensePlateCar(licensePlateCar);
	}

	public boolean existsByParkingSpotNumber(String parkingSpotNumber) {
		return repository.existsByParkingSpotNumber(parkingSpotNumber);
	}

	public boolean existsByApartmentAndBlock(String apartment, String block) {
		return repository.existsByApartmentAndBlock(apartment, block);
	}

	public Page<ParkingSpot> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Optional<ParkingSpot> findById(UUID id) {
		return repository.findById(id);
	}

	@Transactional
	public void delete(ParkingSpot parkingSpotModel) {
		repository.delete(parkingSpotModel);
	}
	
	/*
	public ParkingSpot update(UUID id, ParkingSpotDTO obj) {
		ParkingSpot entity = repository.getReferenceById(id);
		BeanUtils.copyProperties(obj, entity);
		updateData(entity, obj);
		return repository.save(entity);

	}
	*/
	
	public ParkingSpot update(ParkingSpot parkingSpot, ParkingSpotDTO parkingSpotDto, Optional<ParkingSpot> parkingSpotOp) {
		BeanUtils.copyProperties(parkingSpotDto, parkingSpot);
		parkingSpot.setId(parkingSpotOp.get().getId());
		parkingSpot.setRegistrationDate(parkingSpotOp.get().getRegistrationDate());
		repository.save(parkingSpot);
		return parkingSpot;
	}

	/*
	private void updateData(ParkingSpot entity, ParkingSpotDTO obj) {
		entity.setParkingSpotNumber(obj.getParkingSpotNumber());
		entity.setLicensePlateCar(obj.getLicensePlateCar());
		entity.setBrandCar(obj.getBrandCar());
		entity.setModelCar(obj.getModelCar());
		entity.setColorCar(obj.getColorCar());
		entity.setResponsibleName(obj.getResponsibleName());
		entity.setApartment(obj.getApartment());
		entity.setBlock(obj.getBlock());
	}
	*/
}
