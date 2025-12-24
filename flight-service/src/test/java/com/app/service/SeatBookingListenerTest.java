package com.app.service;

import com.app.event.SeatBookedEvent;
import com.app.exception.FlightNotFoundException;
import com.app.model.Flight;
import com.app.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SeatBookingListenerTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private SeatBookingListener listener;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleSeatBooked_shouldReduceSeats() {
        Flight flight = Flight.builder()
                .flightNumber("AI-101")
                .availableSeats(100)
                .build();

        SeatBookedEvent event = new SeatBookedEvent("AI-101", 5,"dsdfds");

        when(flightRepository.findByFlightNumber("AI-101"))
                .thenReturn(Optional.of(flight));

        listener.handleSeatBooked(event);

        assertEquals(95, flight.getAvailableSeats());
        verify(flightRepository).save(flight);
    }

    @Test
    void handleSeatBooked_shouldNotGoNegative() {
        Flight flight = Flight.builder()
                .flightNumber("AI-101")
                .availableSeats(2)
                .build();

        SeatBookedEvent event = new SeatBookedEvent("AI-101", 10,"dfdsfsd");

        when(flightRepository.findByFlightNumber("AI-101"))
                .thenReturn(Optional.of(flight));

        listener.handleSeatBooked(event);

        assertEquals(0, flight.getAvailableSeats()); // clamped
        verify(flightRepository).save(flight);
    }

    @Test
    void handleSeatBooked_shouldThrowWhenFlightNotFound() {
        when(flightRepository.findByFlightNumber("X-999"))
                .thenReturn(Optional.empty());

        SeatBookedEvent event = new SeatBookedEvent("X-999", 3,"sdfds");

        assertThrows(FlightNotFoundException.class,
                () -> listener.handleSeatBooked(event));

        verify(flightRepository, never()).save(any());
    }
}
