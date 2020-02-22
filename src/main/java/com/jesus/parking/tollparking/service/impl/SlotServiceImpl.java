package com.jesus.parking.tollparking.service.impl;

import com.jesus.parking.tollparking.dto.SlotUpdateResource;
import com.jesus.parking.tollparking.model.Parking;
import com.jesus.parking.tollparking.model.Slot;
import com.jesus.parking.tollparking.model.SlotType;
import com.jesus.parking.tollparking.repository.SlotRepository;
import com.jesus.parking.tollparking.service.SlotService;
import com.jesus.parking.tollparking.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
public class SlotServiceImpl implements SlotService {

    @Autowired
    private SlotRepository slotRepository;

    @Override
    public Slot createSlot(Parking parking, SlotType slotType) {
        return slotRepository.saveAndFlush(new Slot(parking, false, slotType));
    }

    @Override
    public List<Slot> createSlots(Parking parking, SlotType slotType, int numberOfAvailableSlots) {
        List<Slot> slots = new ArrayList<>();
        IntStream.rangeClosed(1, numberOfAvailableSlots)
                .forEach(i -> slots.add(createSlot(parking, slotType)));
        return slots;
    }

    @Override
    public List<Slot> getSlotsFromParking(Long parkingId) {
        return slotRepository.findByParkingId(parkingId);
    }

    @Override
    public Slot findSlotByIdAndParkingId(Long slotId, Long parkingId) throws  ResourceNotFoundException{
        return slotRepository.findByIdAndParkingId(slotId, parkingId)
                .orElseThrow(() -> new ResourceNotFoundException("Parking or slot not found"));
    }

    @Override
    public Slot updateSlot(Slot slot, SlotUpdateResource slotUpdateResource) {
        if(slotUpdateResource.getSlotType()!=null) slot.setSlotType(slotUpdateResource.getSlotType());
        slot.setOccupied(slotUpdateResource.isOccupied());
        return slotRepository.saveAndFlush(slot);
    }

    @Override
    public void deleteSlot(Long slotId) {
        slotRepository.deleteById(slotId);
    }

    @Override
    public Slot occupySlot(Long slotId, Long parkingId, String plateNumber) throws  ResourceNotFoundException{
        Optional<Slot> slot = slotRepository.findByIdAndParkingId(slotId, parkingId);
        if(slot.isPresent()){
            slot.get().setOccupied(true);
            slot.get().setPlateNumber(plateNumber);
            slot.get().setOccupiedAt(new Date());
            slotRepository.saveAndFlush(slot.get());
            return slot.get();
        }else{
            throw new ResourceNotFoundException("Slot not found");
        }

    }

    @Override
    public Slot vacantSlot(Long slotId, Long parkingId) {
        Optional<Slot> slot = slotRepository.findByIdAndParkingId(slotId, parkingId);
        if(slot.isPresent()){
            slot.get().setOccupied(false);
            slot.get().setPlateNumber("");
            slotRepository.saveAndFlush(slot.get());
            return slot.get();
        }else{
            throw new ResourceNotFoundException("Slot not found");
        }
    }

    @Override
    public Slot findSlotByParkingIdAndPlateNumber(Long parkingId, String plateNumber) throws ResourceNotFoundException{
        return slotRepository.findByParkingIdAndPlateNumber(parkingId, plateNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Slot and plate number do not match"));
    }

}
