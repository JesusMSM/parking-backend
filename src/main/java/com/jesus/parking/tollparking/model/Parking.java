package com.jesus.parking.tollparking.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "parking")
public class Parking implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parking_generator")
    @SequenceGenerator(name="parking_generator", sequenceName = "parking_seq")
    private long id;

//    @Column(name = "slots", nullable = false)
//    @OneToMany(fetch=FetchType.EAGER, mappedBy = "parking", cascade=CascadeType.ALL, orphanRemoval = true)
//    private List<Slot> slots;

    @Column(name = "pricing_policy", nullable = false)
    @Enumerated(EnumType.STRING)
    private PricingPolicy pricingPolicy;

    @Column(name = "price_per_hour", nullable = false)
    private double pricePerHour;

    @Column(name = "fixed_price", nullable = false)
    private double fixedPrice;

    public Parking(PricingPolicy pricingPolicy, double pricePerHour, double fixedPrice) {
        this.pricingPolicy = pricingPolicy;
        this.pricePerHour = pricePerHour;
        this.fixedPrice = fixedPrice;
    }

    public Parking(){}
}
