package com.app.dto;

import com.app.entity.Booking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseDTO {

    private Long id;
    private String pnr;
    private String flightNumber;
    private List<PassengerDTO> passengers;
    private LocalDateTime bookingDate;
    private Booking.BookingStatus status;
    private Double totalPrice;
    private String contactEmail;
    private String contactPhone;
    private String bookingOwnerEmail;
    private String source;
    private String destination;

}