package com.jesus.parking.tollparking.service;

import com.jesus.parking.tollparking.dto.CarArrivalResource;
import com.jesus.parking.tollparking.dto.CarDepartureResource;
import com.jesus.parking.tollparking.dto.ParkingCreationResource;
import com.jesus.parking.tollparking.dto.ParkingUpdateResource;
import com.jesus.parking.tollparking.model.Parking;

import java.util.List;
import java.util.Optional;

public interface ParkingService {

    List<Parking> getParkings();
    Parking findParkingById(Long parkingId);
    Parking createParking(ParkingCreationResource parkingCreationResource);
    Parking updateParking(Long parkingId, ParkingUpdateResource parkingUpdateResource);
    void deleteParking(Long parkingId);
    Optional<Long> carArrival(Long parkingId, CarArrivalResource carArrivalResource);
    double carDeparture(Long parkingId, CarDepartureResource carDepartureResource);

}
