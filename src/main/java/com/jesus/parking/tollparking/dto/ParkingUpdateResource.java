package com.jesus.parking.tollparking.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jesus.parking.tollparking.model.PricingPolicy;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class ParkingUpdateResource {

    private PricingPolicy pricingPolicy;
    private double pricePerHour;
    private double fixedPrice;

}
