package com.app.service;

import com.app.dto.FlightRequestDTO;
import com.app.dto.FlightResponseDTO;
import com.app.dto.FlightSearchDTO;
import com.app.exception.FlightNotFoundException;
import com.app.model.Flight;
import com.app.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    // Add new flight
    public FlightResponseDTO addFlight(FlightRequestDTO request) {

        Flight flight = Flight.builder()
                .flightNumber(request.getFlightNumber())
                .airline(request.getAirline())
                .source(request.getSource())
                .destination(request.getDestination())
                .departureTime(request.getDepartureTime())
                .arrivalTime(request.getArrivalTime())
                .price(request.getPrice())
                .totalSeats(request.getTotalSeats())
                .availableSeats(request.getTotalSeats())
                .build();

        Flight savedFlight = flightRepository.save(flight);

       return mapToResponseDTO(savedFlight);
    }

    public List<FlightResponseDTO> getAllFlights() {
        List<Flight> allFlights = flightRepository.findAll();
        return allFlights.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

    }
    public FlightResponseDTO getFlightByNumber(String flightNumber) {
        Flight flight = flightRepository.findByFlightNumber(flightNumber)
                .orElseThrow(() -> new FlightNotFoundException(
                        "Flight not found with number: " + flightNumber));

        return mapToResponseDTO(flight);
    }

    public List<FlightResponseDTO> searchFlights(FlightSearchDTO request) {
        String source = request.getSource();
        String destination = request.getDestination();
        String datestr=request.getDate();

        if(datestr!=null && !datestr.equals("")){
            LocalDate date = LocalDate.parse(datestr);
            LocalDateTime start =date.atStartOfDay();
            LocalDateTime end =date.atTime(LocalTime.MAX);

            List<Flight>flights=flightRepository
                    .findBySourceAndDestinationAndDepartureTimeBetween(
                            source,destination,start,end
                    );
            return flights.stream()
                    .map(this::mapToResponseDTO)
                    .collect(Collectors.toList());
        }else{
            // Search only by source and destination incase no flights found for that day
            List<Flight> flights = flightRepository
                    .findBySourceAndDestination(source, destination);

            return flights.stream()
                    .map(this::mapToResponseDTO)
                    .collect(Collectors.toList());
        }

    }


    private FlightResponseDTO mapToResponseDTO(Flight flight) {
        return FlightResponseDTO.builder()
                .id(flight.getId())
                .flightNumber(flight.getFlightNumber())
                .airline(flight.getAirline())
                .source(flight.getSource())
                .destination(flight.getDestination())
                .departureTime(flight.getDepartureTime())
                .arrivalTime(flight.getArrivalTime())
                .price(flight.getPrice())
                .availableSeats(flight.getAvailableSeats())
                .totalSeats(flight.getTotalSeats())
                .build();
    }


}
