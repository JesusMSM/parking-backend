package com.jesus.parking.tollparking.service;

import com.jesus.parking.tollparking.dto.SlotUpdateResource;
import com.jesus.parking.tollparking.model.Parking;
import com.jesus.parking.tollparking.model.PricingPolicy;
import com.jesus.parking.tollparking.model.Slot;
import com.jesus.parking.tollparking.model.SlotType;
import com.jesus.parking.tollparking.repository.SlotRepository;
import com.jesus.parking.tollparking.service.impl.SlotServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.Any;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SlotServiceTest {

    @Mock
    SlotRepository slotRepository;

    @InjectMocks
    SlotServiceImpl slotService;

    @Test
    public void getSlotsFromParking(){
        List<Slot> list = new ArrayList<>();
        Parking parkingOne = new Parking(PricingPolicy.FIXED, 3.0, 10);

        Slot slotOne = new Slot(parkingOne, false, SlotType.GASOLINE);
        Slot slotTwo = new Slot(parkingOne, false, SlotType.KW20);
        Slot slotThree = new Slot(parkingOne,  true, SlotType.KW50);

        list.add(slotOne);
        list.add(slotTwo);
        list.add(slotThree);

        when(slotRepository.findByParkingId(parkingOne.getId())).thenReturn(list);

        List<Slot> slots = slotService.getSlotsFromParking(parkingOne.getId());

        assertEquals(3, slots.size());
        verify(slotRepository, times(1)).findByParkingId(parkingOne.getId());

    }

    @Test
    public void createSlot(){
        Parking parkingOne = new Parking(PricingPolicy.FIXED, 3.0, 10);
        Slot slotOne = new Slot(parkingOne, false, SlotType.GASOLINE);

        when(slotRepository.saveAndFlush(slotOne)).thenReturn(slotOne);
        Slot slot = slotService.createSlot(parkingOne, SlotType.GASOLINE);
        assertEquals(slot, slotOne);
    }

    @Test
    public void createSlots(){
        Parking parkingOne = new Parking(PricingPolicy.FIXED, 3.0, 10);
        Slot slotOne = new Slot(parkingOne, false, SlotType.GASOLINE);

        when(slotRepository.saveAndFlush(slotOne)).thenReturn(slotOne);
        List<Slot> slot = slotService.createSlots(parkingOne, SlotType.GASOLINE, 3);
        assertEquals(3, slot.size());
    }

    @Test
    public void findSlotByIdAndParkingId(){

        Parking parkingOne = new Parking(PricingPolicy.FIXED, 3.0, 10);
        Slot slotOne = new Slot(parkingOne, false, SlotType.GASOLINE);

        when(slotRepository.findByIdAndParkingId(slotOne.getId(), parkingOne.getId())).thenReturn(Optional.of(slotOne));

        Slot slot = slotService.findSlotByIdAndParkingId(slotOne.getId(), parkingOne.getId());

        assertEquals(slot, slotOne);
    }

    @Test
    public void updateSlot(){

        Parking parkingOne = new Parking(PricingPolicy.FIXED, 3.0, 10);
        Slot slotOne = new Slot(parkingOne, false, SlotType.GASOLINE);

        SlotUpdateResource resource = new SlotUpdateResource();
        resource.setSlotType(SlotType.KW50);
        resource.setOccupied(true);
        Slot slotUpdated = new Slot(parkingOne, resource.isOccupied(), resource.getSlotType());

        when(slotRepository.saveAndFlush(any(Slot.class))).thenReturn(slotUpdated);

        Slot slot = slotService.updateSlot(slotOne, resource);

        assertTrue(slot.isOccupied());
        assertEquals(SlotType.KW50, slot.getSlotType());

    }

    @Test
    public void deleteSlot(){
        Parking parkingOne = new Parking(PricingPolicy.FIXED, 3.0, 10);
        Slot slotOne = new Slot(parkingOne, false, SlotType.GASOLINE);

        Assertions.assertThatCode(() -> slotService.deleteSlot(slotOne.getId()))
                .doesNotThrowAnyException();
    }

    @Test
    public void occupySlot(){
        Parking parkingOne = new Parking(PricingPolicy.FIXED, 3.0, 10);
        Slot slotOne = new Slot(parkingOne, false, SlotType.GASOLINE);


        when(slotRepository.findByIdAndParkingId(slotOne.getId(), parkingOne.getId())).thenReturn(Optional.of(slotOne));
        when(slotRepository.saveAndFlush(any(Slot.class))).thenReturn(slotOne);

        Slot slot = slotService.occupySlot(slotOne.getId(), parkingOne.getId(), "ABC");
        assertTrue(slot.isOccupied());
        assertEquals("ABC", slot.getPlateNumber());
    }

    @Test
    public void vacantSlot(){
        Parking parkingOne = new Parking(PricingPolicy.FIXED, 3.0, 10);
        Slot slotOne = new Slot(parkingOne, false, SlotType.GASOLINE);


        when(slotRepository.findByIdAndParkingId(slotOne.getId(), parkingOne.getId())).thenReturn(Optional.of(slotOne));
        when(slotRepository.saveAndFlush(any(Slot.class))).thenReturn(slotOne);

        Slot slot = slotService.vacantSlot(slotOne.getId(), parkingOne.getId());
        assertTrue(!slot.isOccupied());
        assertEquals("", slot.getPlateNumber());
    }

    @Test
    public void findSlotByParkingIdAndPlateNumber(){

        Parking parkingOne = new Parking(PricingPolicy.FIXED, 3.0, 10);
        Slot slotOne = new Slot(parkingOne, false, SlotType.GASOLINE);

        slotOne.setPlateNumber("ABC");
        when(slotRepository.findByParkingIdAndPlateNumber(slotOne.getId(), "ABC")).thenReturn(Optional.of(slotOne));

        Slot slot = slotService.findSlotByParkingIdAndPlateNumber(slotOne.getId(), "ABC");

        assertEquals("ABC", slot.getPlateNumber());
    }

}
