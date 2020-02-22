package com.jesus.parking.tollparking.service;

import com.jesus.parking.tollparking.dto.ParkingCreationResource;
import com.jesus.parking.tollparking.dto.ParkingUpdateResource;
import com.jesus.parking.tollparking.model.Parking;
import com.jesus.parking.tollparking.model.PricingPolicy;
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
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
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

}
