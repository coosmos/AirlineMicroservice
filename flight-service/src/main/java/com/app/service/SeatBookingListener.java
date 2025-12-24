package com.app.service;

import com.app.model.Flight;
import com.app.event.SeatBookedEvent;
import com.app.exception.FlightNotFoundException;
import com.app.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeatBookingListener {

    @Autowired
    private FlightRepository flightRepository;

    @KafkaListener(topics = "seat-booked-topic", groupId = "flight-service-group")
    @Transactional
    public void handleSeatBooked(SeatBookedEvent event) {
        System.out.println(" Kafka event received: " + event);

        // Find flight
        Flight flight = flightRepository.findByFlightNumber(event.getFlightNumber())
                .orElseThrow(() -> new FlightNotFoundException(
                        "Flight not found: " + event.getFlightNumber()));

        // Reduce available seats
        int currentSeats = flight.getAvailableSeats();
        int newSeats = currentSeats - event.getSeatsBooked();

        if (newSeats < 0) {
            System.out.println(" Warning: Seat count went negative for flight: "
                    + event.getFlightNumber());
            newSeats = 0;
        }

        flight.setAvailableSeats(newSeats);
        flightRepository.save(flight);

        System.out.println("Seats updated for flight " + event.getFlightNumber()
                + ": " + currentSeats + " → " + newSeats);
    }
}