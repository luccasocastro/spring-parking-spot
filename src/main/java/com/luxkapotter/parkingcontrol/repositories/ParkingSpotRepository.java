package com.luxkapotter.parkingcontrol.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luxkapotter.parkingcontrol.models.ParkingSpot;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, UUID>{
	
	boolean existsByLicensePlateCar(String licensePlateCar);
	boolean existsByParkingSpotNumber(String parkingSpotNumber);
	boolean existsByApartmentAndBlock(String apartment, String block);
}
