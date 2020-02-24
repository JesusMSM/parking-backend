package com.jesus.parking.tollparking.controller;

import com.jesus.parking.tollparking.dto.SlotCreationResource;
import com.jesus.parking.tollparking.dto.SlotUpdateResource;
import com.jesus.parking.tollparking.model.Parking;
import com.jesus.parking.tollparking.model.Slot;
import com.jesus.parking.tollparking.service.ParkingService;
import com.jesus.parking.tollparking.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class SlotController {

    @Autowired
    private SlotService slotService;

    @Autowired
    private ParkingService parkingService;

    @GetMapping("/api/parkings/{parkingId}/slots")
    public List<Slot> findAll(@PathVariable Long slotId) {
        return slotService.getSlotsFromParking(slotId);
    }

    @PostMapping("/api/parkings/{parkingId}/slots/create")
    public List<Slot> createSlot(@PathVariable Long parkingId,
                                 @RequestBody @Valid SlotCreationResource slotCreationResource) {
        Parking parking = parkingService.findParkingById(parkingId);
        return slotService.createSlots(parking, slotCreationResource.getSlotType(), slotCreationResource.getNumberOfAvailableSlots());
    }

    @PutMapping("/api/parkings/{parkingId}/slots/{slotId}/update")
    public Slot updateSlot(@PathVariable Long parkingId,
                           @PathVariable Long slotId,
                           @RequestBody @Valid SlotUpdateResource slotUpdateResource) {
        Slot slot = slotService.findSlotByIdAndParkingId(slotId, parkingId);
        return slotService.updateSlot(slot, slotUpdateResource);
    }

    @DeleteMapping("/api/parkings/{parkingId}/slots/{slotId}/delete")
    public ResponseEntity<?> deleteSlot(@PathVariable Long parkingId,
                                    @PathVariable Long slotId) {
        if(parkingService.findParkingById(parkingId)!=null) slotService.deleteSlot(slotId);
        return ResponseEntity.ok().build();
    }

}
