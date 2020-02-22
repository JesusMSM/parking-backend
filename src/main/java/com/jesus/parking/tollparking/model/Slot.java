package com.jesus.parking.tollparking.model;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "slot")
public class Slot implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "slot_generator")
    @SequenceGenerator(name="slot_generator", sequenceName = "slot_seq")
    private long id;

    @Column(name = "occupied", nullable = false)
    private boolean occupied;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private SlotType slotType;

    @Column(name = "plate_number", nullable = false)
    private String plateNumber = "";

    @Column(name = "occupied_at")
    private Date occupiedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parking_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Parking parking;

    public Slot(Parking parking, boolean occupied, SlotType slotType){
        this.parking = parking;
        this.occupied = occupied;
        this.slotType = slotType;
    }

    public Slot(){}

}
