package com.app.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingConfirmedEvent {

    private String pnr;
    private String flightNumber;
    private String airline;
    private String source;
    private String destination;
    private LocalDateTime departureTime;
    private List<PassengerInfo> passengers;
    private Double totalPrice;
    private String contactEmail;
    private String contactPhone;
}