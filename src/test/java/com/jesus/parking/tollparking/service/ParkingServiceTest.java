package com.jesus.parking.tollparking.service;

import com.jesus.parking.tollparking.dto.CarArrivalResource;
import com.jesus.parking.tollparking.dto.CarDepartureResource;
import com.jesus.parking.tollparking.dto.ParkingCreationResource;
import com.jesus.parking.tollparking.dto.ParkingUpdateResource;
import com.jesus.parking.tollparking.model.Parking;
import com.jesus.parking.tollparking.model.PricingPolicy;
import com.jesus.parking.tollparking.model.Slot;
import com.jesus.parking.tollparking.model.SlotType;
import com.jesus.parking.tollparking.repository.ParkingRepository;
import com.jesus.parking.tollparking.service.impl.ParkingServiceImpl;
import com.jesus.parking.tollparking.service.impl.SlotServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.Any;
import org.mockito.junit.MockitoJUnitRunner;

import javax.swing.text.html.Option;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class ParkingServiceTest {

    @Mock
    ParkingRepository parkingRepository;

    @Mock
    SlotServiceImpl slotService;

    @InjectMocks
    ParkingServiceImpl parkingServiceImpl;


    @Test
    public void getParkings()
    {
        List<Parking> list = new ArrayList<>();
        Parking parkingOne = new Parking(PricingPolicy.FIXED, 3.0, 10);
        Parking parkingTwo = new Parking(PricingPolicy.FIXED, 4.0, 25);
        Parking parkingThree = new Parking(PricingPolicy.DYNAMIC, 3.0, 0.0);

        list.add(parkingOne);
        list.add(parkingTwo);
        list.add(parkingThree);

        when(parkingRepository.findAll()).thenReturn(list);

        List<Parking> empList = parkingServiceImpl.getParkings();

        assertEquals(3, empList.size());
        verify(parkingRepository, times(1)).findAll();
    }

    @Test
    public void findParkingById()
    {
        Parking parkingOne = new Parking(PricingPolicy.FIXED, 3.0, 10);

        when(parkingRepository.findById(1L)).thenReturn(Optional.of(parkingOne));
        Parking parking = parkingServiceImpl.findParkingById(1L);
        assertEquals(parking, parkingOne);
    }

    @Test
    public void createParking()
    {
        ParkingCreationResource resource = new ParkingCreationResource();
        resource.setFixedPrice(10);
        resource.setPricePerHour(3.0);
        resource.setPricingPolicy(PricingPolicy.FIXED);
        resource.setSlots(new ArrayList<>());
        Parking parkingOne = new Parking(
                resource.getPricingPolicy(),
                resource.getPricePerHour(),
                resource.getFixedPrice());

        when(parkingRepository.saveAndFlush(parkingOne)).thenReturn(parkingOne);
        Parking parking = parkingServiceImpl.createParking(resource);
        assertEquals(parking, parkingOne);
    }

    @Test
    public void updateParking()
    {
        ParkingUpdateResource resource = new ParkingUpdateResource();
        resource.setPricePerHour(5.0);
        resource.setPricingPolicy(PricingPolicy.DYNAMIC);
        resource.setFixedPrice(1.0);

        Parking parkingOne = new Parking(PricingPolicy.FIXED, 3.0, 10);

        when(parkingRepository.findById(1L)).thenReturn(Optional.of(parkingOne));

        Parking parkingUpdated = new Parking(resource.getPricingPolicy(), resource.getPricePerHour(), resource.getFixedPrice());

        when(parkingRepository.saveAndFlush(parkingUpdated)).thenReturn(parkingUpdated);
        Parking parking = parkingServiceImpl.updateParking(1L, resource);
        assertEquals(parking, parkingUpdated);
    }

    @Test
    public void deleteParking()
    {
        Parking parkingOne = new Parking(PricingPolicy.FIXED, 3.0, 10);

        when(parkingRepository.findById(parkingOne.getId())).thenReturn(Optional.of(parkingOne));

        Assertions.assertThatCode(() -> parkingServiceImpl.deleteParking(parkingOne.getId()))
                .doesNotThrowAnyException();
    }

    @Test
    public void carArrival(){
        Parking parkingOne = new Parking(PricingPolicy.FIXED, 3.0, 10);
        List<Slot> list = new ArrayList<>();

        Slot slotOne = new Slot(parkingOne, false, SlotType.GASOLINE);
        Slot slotTwo = new Slot(parkingOne, true, SlotType.KW20);
        Slot slotThree = new Slot(parkingOne,  false, SlotType.KW50);

        list.add(slotOne);
        list.add(slotTwo);
        list.add(slotThree);

        CarArrivalResource resource = new CarArrivalResource();
        resource.setCarType(SlotType.KW50);
        resource.setPlateNumber("ABC");
        when(slotService.getSlotsFromParking(any())).thenReturn(list);
        Optional<Long> slotId = parkingServiceImpl.carArrival(parkingOne.getId(), resource);
        assertEquals(slotThree.getId(), slotId.get());

        resource.setCarType(SlotType.KW20);
        Optional<Long> slotId2 = parkingServiceImpl.carArrival(parkingOne.getId(), resource);
        assertTrue(!slotId2.isPresent());

    }

    @Test
    public void carDeparture(){
        Parking parkingOne = new Parking(PricingPolicy.FIXED, 3.0, 10);

        Slot slotOne = new Slot(parkingOne, true, SlotType.GASOLINE);

        slotOne.setPlateNumber("ABC");
        slotOne.setOccupiedAt(new Date(System.currentTimeMillis() - 3600 * 1000)); // 1 Hour parking

        CarDepartureResource resource = new CarDepartureResource();
        resource.setPlateNumber("ABC");
        when(slotService.findSlotByParkingIdAndPlateNumber(parkingOne.getId(),"ABC"))
                .thenReturn(slotOne);
        when(parkingRepository.findById(parkingOne.getId()))
                .thenReturn(Optional.of(parkingOne));
        double price = parkingServiceImpl.carDeparture(parkingOne.getId(), resource);
        assertEquals(13.0, price);
    }

}
