package com.app.controller;


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

    @PostMapping
    public ResponseEntity<?> addFlight( @Valid @RequestBody FlightRequestDTO request) {
        FlightResponseDTO response= flightService.addFlight(request);
      return  new ResponseEntity<>(HttpStatus.CREATED); //note for future , responseEntity has a factory method that returns an object
    }

    @GetMapping
    public ResponseEntity<List<FlightResponseDTO>> getFlights() {
        return ResponseEntity.ok(flightService.getAllFlights());
    }

    @GetMapping("/search")
    public ResponseEntity<List<FlightResponseDTO>> searchFlights(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam(required = false) String date) {

        FlightSearchDTO searchDTO = new FlightSearchDTO(source, destination, date);
        List<FlightResponseDTO> flights = flightService.searchFlights(searchDTO);

        return ResponseEntity.ok(flights);
    }

    @GetMapping("/{flightNumber}")
    public ResponseEntity<FlightResponseDTO> getFlightByNumber(
            @PathVariable String flightNumber) {

        FlightResponseDTO flight = flightService.getFlightByNumber(flightNumber);
        return ResponseEntity.ok(flight);
    }


}

