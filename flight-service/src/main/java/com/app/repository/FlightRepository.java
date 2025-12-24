package com.app.repository;

import com.app.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {

    Optional<Flight> findByFlightNumber(String flightNumber);

    List<Flight> findBySourceAndDestinationAndDepartureTimeBetween(String source, String destination, LocalDateTime start, LocalDateTime end);

    List<Flight> findBySourceAndDestination(String source, String destination);
}
