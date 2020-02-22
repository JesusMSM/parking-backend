package com.jesus.parking.tollparking.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jesus.parking.tollparking.model.Parking;
import com.jesus.parking.tollparking.model.SlotType;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class SlotUpdateResource {
    private boolean occupied;
    private SlotType slotType;
}
