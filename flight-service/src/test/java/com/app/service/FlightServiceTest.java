package com.app.service;

import com.app.dto.FlightRequestDTO;
import com.app.dto.FlightResponseDTO;
import com.app.dto.FlightSearchDTO;
import com.app.exception.FlightNotFoundException;
import com.app.model.Flight;
import com.app.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addFlight_shouldSaveAndReturnDTO() {
        FlightRequestDTO req = new FlightRequestDTO();
        req.setFlightNumber("AI-101");
        req.setAirline("Air India");
        req.setSource("DEL");
        req.setDestination("BOM");
        req.setDepartureTime(LocalDateTime.now().plusDays(1));
        req.setArrivalTime(LocalDateTime.now().plusDays(1).plusHours(2));
        req.setPrice(5000.0);
        req.setTotalSeats(180);

        Flight saved = Flight.builder()
                .id(1L)
                .flightNumber(req.getFlightNumber())
                .airline(req.getAirline())
                .source(req.getSource())
                .destination(req.getDestination())
                .departureTime(req.getDepartureTime())
                .arrivalTime(req.getArrivalTime())
                .price(req.getPrice())
                .totalSeats(180)
                .availableSeats(180)
                .build();

        when(flightRepository.save(any(Flight.class))).thenReturn(saved);

        FlightResponseDTO response = flightService.addFlight(req);

        assertThat(response.getFlightNumber()).isEqualTo("AI-101");
        assertThat(response.getAvailableSeats()).isEqualTo(180);

        verify(flightRepository, times(1)).save(any());
    }

    @Test
    void getAllFlights_shouldReturnMappedDTOList() {
        Flight flight1 = Flight.builder().flightNumber("AI-101").build();
        Flight flight2 = Flight.builder().flightNumber("AI-202").build();

        when(flightRepository.findAll()).thenReturn(List.of(flight1, flight2));

        List<FlightResponseDTO> result = flightService.getAllFlights();

        assertThat(result).hasSize(2);
        verify(flightRepository, times(1)).findAll();
    }

    @Test
    void getFlightByNumber_shouldReturnFlight() {
        Flight f = Flight.builder().flightNumber("AI-101").build();

        when(flightRepository.findByFlightNumber("AI-101"))
                .thenReturn(Optional.of(f));

        FlightResponseDTO result = flightService.getFlightByNumber("AI-101");

        assertThat(result.getFlightNumber()).isEqualTo("AI-101");
    }

    @Test
    void getFlightByNumber_shouldThrow_whenNotFound() {
        when(flightRepository.findByFlightNumber("WRONG"))
                .thenReturn(Optional.empty());

        assertThrows(FlightNotFoundException.class,
                () -> flightService.getFlightByNumber("WRONG"));
    }

    @Test
    void searchFlights_withDate_shouldCallDateRangeQuery() {
        FlightSearchDTO req = new FlightSearchDTO("DEL", "BOM", "2025-12-10");

        LocalDate date = LocalDate.parse("2025-12-10");
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        Flight flight = Flight.builder().flightNumber("AI-101").build();

        when(flightRepository
                .findBySourceAndDestinationAndDepartureTimeBetween("DEL", "BOM", start, end))
                .thenReturn(List.of(flight));

        List<FlightResponseDTO> result = flightService.searchFlights(req);

        assertThat(result).hasSize(1);

        verify(flightRepository).findBySourceAndDestinationAndDepartureTimeBetween(
                eq("DEL"),
                eq("BOM"),
                eq(start),
                eq(end)
        );
    }

    @Test
    void searchFlights_withoutDate_shouldCallSimpleQuery() {
        FlightSearchDTO req = new FlightSearchDTO("DEL", "BOM", "");

        Flight f1 = Flight.builder().flightNumber("AI-111").build();

        when(flightRepository.findBySourceAndDestination("DEL", "BOM"))
                .thenReturn(List.of(f1));

        List<FlightResponseDTO> result = flightService.searchFlights(req);

        assertThat(result).hasSize(1);
        verify(flightRepository).findBySourceAndDestination("DEL", "BOM");
    }
}
