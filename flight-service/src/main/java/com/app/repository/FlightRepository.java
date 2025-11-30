package com.app.repository;

import com.app.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Integer> {

    Optional<Flight> findByFlightNumber(String flightNumber);

    List<Flight> findBySourceAndDestinationAndDepartureTimeBetween(String source, String destination, LocalDateTime start, LocalDateTime end);

}
