package com.jesus.parking.tollparking.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jesus.parking.tollparking.model.SlotType;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class SlotCreationResource {

    private SlotType slotType;
    private int numberOfAvailableSlots;

}
