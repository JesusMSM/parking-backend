package com.jesus.parking.tollparking.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jesus.parking.tollparking.model.PricingPolicy;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class ParkingCreationResource {

    private Long id;
    private List<SlotCreationResource> slots;
    private PricingPolicy pricingPolicy;
    private double pricePerHour;
    private double fixedPrice;

}
