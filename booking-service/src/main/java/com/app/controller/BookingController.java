package com.app.controller;

import com.app.dto.BookingRequestDTO;
import com.app.dto.BookingResponseDTO;
import com.app.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // Create new booking
    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(
            @Valid @RequestBody BookingRequestDTO requestDTO) {

        BookingResponseDTO response = bookingService.createBooking(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Get all bookings
    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings() {
        List<BookingResponseDTO> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    // Get booking by PNR
    @GetMapping("/{pnr}")
    public ResponseEntity<BookingResponseDTO> getBookingByPnr(@PathVariable String pnr) {
        BookingResponseDTO booking = bookingService.getBookingByPnr(pnr);
        return ResponseEntity.ok(booking);
    }

    // Optional: Cancel booking
    @PutMapping("/{pnr}/cancel")
    public ResponseEntity<BookingResponseDTO> cancelBooking(@PathVariable String pnr) {
        BookingResponseDTO response = bookingService.cancelBooking(pnr);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/healthCheck")
    public String healthCheck() {
        return "OK";
    }
}