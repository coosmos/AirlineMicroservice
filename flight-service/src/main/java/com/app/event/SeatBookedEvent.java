package com.app.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatBookedEvent {
    private String flightNumber;
    private Integer seatsBooked;
    private String pnr;
}