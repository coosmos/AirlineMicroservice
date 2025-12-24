package com.app.controller;


import com.app.amadeus.AmadeusAuthService;
import com.app.amadeus.AmadeusFlightResponseDTO;
import com.app.amadeus.AmadeusFlightService;
import com.app.dto.FlightRequestDTO;
import com.app.dto.FlightResponseDTO;
import com.app.dto.FlightSearchDTO;
import com.app.model.Flight;
import com.app.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flight")
public class FlightController {


    @Autowired
    private  FlightService flightService;

    @Autowired
    private AmadeusAuthService authService;
    @Autowired
    private AmadeusFlightService amadeusflightService;

    @PostMapping
    public ResponseEntity<?> addFlight( @Valid @RequestBody FlightRequestDTO request) {
        FlightResponseDTO response= flightService.addFlight(request);
      return  new ResponseEntity<>(HttpStatus.CREATED); //note for future , responseEntity has a factory method that returns an object
    }

    @GetMapping
    public ResponseEntity<List<FlightResponseDTO>> getFlights() {
        return ResponseEntity.ok(flightService.getAllFlights());
    }


    @PostMapping("/search")
    public ResponseEntity<List<FlightResponseDTO>> searchFlights(
            @Valid @RequestBody FlightSearchDTO searchDTO) {

        List<FlightResponseDTO> flights = flightService.searchFlights(searchDTO);
        return ResponseEntity.ok(flights);
    }


    @GetMapping("/{flightNumber}")
    public ResponseEntity<FlightResponseDTO> getFlightByNumber(
            @PathVariable String flightNumber) {

        FlightResponseDTO flight = flightService.getFlightByNumber(flightNumber);
        return ResponseEntity.ok(flight);
    }


    @GetMapping("/offers")
    public List<AmadeusFlightResponseDTO> offers(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam String date
    ) throws Exception {
        return amadeusflightService.searchFlightOffers(origin, destination, date);
    }


}

