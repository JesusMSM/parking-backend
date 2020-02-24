package com.jesus.parking.tollparking.controller;

import com.jesus.parking.tollparking.dto.CarArrivalResource;
import com.jesus.parking.tollparking.dto.CarDepartureResource;
import com.jesus.parking.tollparking.dto.ParkingCreationResource;
import com.jesus.parking.tollparking.dto.ParkingUpdateResource;
import com.jesus.parking.tollparking.model.Parking;
import com.jesus.parking.tollparking.service.ParkingService;
import com.jesus.parking.tollparking.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    @GetMapping("/api/parkings")
    public Iterable<Parking> findAll() {
        return parkingService.getParkings();
    }

    @GetMapping("/api/parkings/{parkingId}")
    public Parking findParkingById(@PathVariable Long parkingId){
        return parkingService.findParkingById(parkingId);
    }

    @PostMapping("/api/parkings/create")
    public Parking createParking(@RequestBody ParkingCreationResource parkingCreationResource) {
        return parkingService.createParking(parkingCreationResource);
    }

    @PutMapping("/api/parkings/{parkingId}/update")
    public Parking updateParking(@PathVariable Long parkingId, @RequestBody @Valid ParkingUpdateResource parkingUpdateResource){
        return parkingService.updateParking(parkingId, parkingUpdateResource);
    }

    @DeleteMapping("/api/parkings/{parkingId}/update")
    public ResponseEntity<?> deleteParking(@PathVariable Long parkingId){
        parkingService.deleteParking(parkingId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/parkings/{parkingId}/carArrival")
    public Map<String, String> carArrival(@PathVariable Long parkingId, @RequestBody @Valid CarArrivalResource carArrivalResource) {
        HashMap<String, String> response = new HashMap<>();
        Optional<Long> slotId = parkingService.carArrival(parkingId, carArrivalResource);
        if(slotId.isPresent()){
            response.put("value", "Proceed to slot number: " + slotId.get());
        }else{
            response.put("value", "There is no more available slots in this parking for your car type");
        }
        return response;
    }

    @PostMapping("/api/parkings/{parkingId}/carDeparture")
    public Map<String, String> carLeaves(@PathVariable Long parkingId, @RequestBody @Valid CarDepartureResource carDepartureResource) {
        HashMap<String, String> response = new HashMap<>();
        double price = parkingService.carDeparture(parkingId, carDepartureResource);
        response.put("value", "Thank you for using our parking. The total amount of your bill is " + price);
        return response;
    }

}
