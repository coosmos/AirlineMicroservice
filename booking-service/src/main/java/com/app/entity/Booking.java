package com.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking  extends  BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String passengerName;

    @Column(nullable = false)
    private String Pnr;

    @Column(nullable = false)
    private String flightNumber;  // Coming from flight-service

    @Column(nullable = false)
    private LocalDateTime bookingTime;

    @Column(nullable = false)
    private BookingStatus status=BookingStatus.CREATED;

    @Column(nullable = false)
    private String Email;
    // private Integer seats;


    public enum BookingStatus{
        CREATED,
        ACTIVE,
        CANCELLED

    }
}
