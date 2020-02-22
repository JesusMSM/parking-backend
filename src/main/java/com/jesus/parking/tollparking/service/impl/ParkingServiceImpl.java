package com.jesus.parking.tollparking.service.impl;

import com.jesus.parking.tollparking.dto.CarArrivalResource;
import com.jesus.parking.tollparking.dto.CarDepartureResource;
import com.jesus.parking.tollparking.dto.ParkingCreationResource;
import com.jesus.parking.tollparking.dto.ParkingUpdateResource;
import com.jesus.parking.tollparking.model.Parking;
import com.jesus.parking.tollparking.model.PricingPolicy;
import com.jesus.parking.tollparking.model.Slot;
import com.jesus.parking.tollparking.repository.ParkingRepository;
import com.jesus.parking.tollparking.service.ParkingService;
import com.jesus.parking.tollparking.service.SlotService;
import com.jesus.parking.tollparking.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParkingServiceImpl implements ParkingService {

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private SlotService slotService;


    @Override
    public List<Parking> getParkings() {
        return parkingRepository.findAll();
    }

    @Override
    public Parking findParkingById(Long parkingId) throws ResourceNotFoundException {
        return parkingRepository.findById(parkingId)
                .orElseThrow(() -> new ResourceNotFoundException("Parking not found"));
    }

    @Override
    public Parking createParking(ParkingCreationResource parkingCreationResource) {
        Parking savedEntity = parkingRepository.saveAndFlush(
                new Parking(
                        parkingCreationResource.getPricingPolicy(),
                        parkingCreationResource.getPricePerHour(),
                        parkingCreationResource.getFixedPrice()));
        parkingCreationResource.getSlots()
                .forEach(slot -> slotService.createSlots(savedEntity, slot.getSlotType(), slot.getNumberOfAvailableSlots()));

        return savedEntity;
    }

    @Override
    public Parking updateParking(Long parkingId, ParkingUpdateResource parkingUpdateResource) {
        Parking parking = findParkingById(parkingId);
        parking.setPricingPolicy(parkingUpdateResource.getPricingPolicy());
        parking.setPricePerHour(parkingUpdateResource.getPricePerHour());
        parking.setFixedPrice(parkingUpdateResource.getFixedPrice());
        return parkingRepository.saveAndFlush(parking);
    }

    @Override
    public void deleteParking(Long parkingId) {
        Parking parking = findParkingById(parkingId);
        parkingRepository.delete(parking);
    }

    @Override
    public Optional<Long> carArrival(Long parkingId, CarArrivalResource carArrivalResource) {
        Optional<Long> slotId = slotService.getSlotsFromParking(parkingId).stream()
                .filter(slot -> slot.getSlotType().equals(carArrivalResource.getCarType()) && !slot.isOccupied())
                .map(Slot::getId).findFirst();

        slotId.ifPresent(aLong -> slotService.occupySlot(aLong, parkingId, carArrivalResource.getPlateNumber()));
        return slotId;
    }

    @Override
    public double carDeparture(Long parkingId, CarDepartureResource carDepartureResource) {
        Slot slot = slotService.findSlotByParkingIdAndPlateNumber(parkingId, carDepartureResource.getPlateNumber());
        slotService.vacantSlot(slot.getId(), parkingId);
        Parking parking = findParkingById(parkingId);

        return billCustomer(
                parking.getPricingPolicy(),
                parking.getPricePerHour(),
                parking.getFixedPrice(),
                hoursDifference(new Date(), slot.getOccupiedAt()));
    }

    private double billCustomer(PricingPolicy pricingPolicy, double pricePerHour, double fixedPrice, double hoursAtParking){
        switch (pricingPolicy){
            case FIXED:
                return fixedPrice + hoursAtParking * pricePerHour;
            default:
               return hoursAtParking * pricePerHour;
        }
    }

    private int hoursDifference(Date date1, Date date2) {

        final int MILLI_TO_HOUR = 1000 * 60 * 60;
        int hours = (int) (date1.getTime() - date2.getTime()) / MILLI_TO_HOUR;
        return hours > 0 ? hours : 1;
    }


}
