package com.jesus.parking.tollparking.service;

import com.jesus.parking.tollparking.dto.SlotUpdateResource;
import com.jesus.parking.tollparking.model.Parking;
import com.jesus.parking.tollparking.model.Slot;
import com.jesus.parking.tollparking.model.SlotType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SlotService {
    Slot createSlot(Parking parking, SlotType slotType);
    List<Slot> createSlots(Parking parking, SlotType slotType, int numberOfAvailableSlots);
    List<Slot> getSlotsFromParking(Long parkingId);
    Slot findSlotByIdAndParkingId(Long slotId, Long parkingId);
    Slot updateSlot(Slot slot, SlotUpdateResource slotUpdateResource);
    void deleteSlot(Long slotId);
    Slot occupySlot(Long slotId, Long parkingId, String plateNumber);
    Slot vacantSlot(Long slotId, Long parkingId);
    Slot findSlotByParkingIdAndPlateNumber(Long parkingId, String plateNumber);

}
