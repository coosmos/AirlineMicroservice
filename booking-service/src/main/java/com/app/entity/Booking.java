package com.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String pnr;

    @Column(nullable = false)
    private String flightNumber;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Passenger> passengers = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime bookingDate;

    @Column(nullable = false)
    private BookingStatus status=BookingStatus.CONFIRMED; // CONFIRMED, CANCELLED, PENDING

    @Column(nullable = false)
    private Double totalPrice;

    private String contactEmail;

    private String contactPhone;

    public enum BookingStatus {
        CONFIRMED,
        CANCELLED,
        PENDING
    }
}