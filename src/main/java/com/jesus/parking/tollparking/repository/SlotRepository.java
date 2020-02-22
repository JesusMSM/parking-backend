package com.jesus.parking.tollparking.repository;

import com.jesus.parking.tollparking.model.Slot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long> {
    List<Slot> findByParkingId(Long parkingId);
    Optional<Slot> findByIdAndParkingId(Long id, Long parkingId);
    Optional<Slot> findByParkingIdAndPlateNumber(Long parkingId, String plateNumber);
}
