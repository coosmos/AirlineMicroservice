package com.app.controller;

import com.app.dto.FlightRequestDTO;
import com.app.dto.FlightResponseDTO;
import com.app.service.FlightService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlightController.class)
class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightService flightService;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    void addFlight_shouldReturn201Created() throws Exception {
        FlightRequestDTO requestDTO = new FlightRequestDTO();
        requestDTO.setFlightNumber("AI-101"); // valid format
        requestDTO.setAirline("Air India");
        requestDTO.setSource("DEL");
        requestDTO.setDestination("BOM");
        requestDTO.setDepartureTime(LocalDateTime.now().plusDays(1)); // in future
        requestDTO.setArrivalTime(LocalDateTime.now().plusDays(1).plusHours(2)); // also in future
        requestDTO.setPrice(5500.0);
        requestDTO.setTotalSeats(180);

        FlightResponseDTO responseDTO = new FlightResponseDTO();

        Mockito.when(flightService.addFlight(any())).thenReturn(responseDTO);

        mockMvc.perform(
                        post("/api/flight")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO))
                )
                .andExpect(status().isCreated());

        verify(flightService, times(1)).addFlight(any());
    }


    @Test
    void getFlights_shouldReturnListOfFlights() throws Exception {
        // given
        FlightResponseDTO flight1 = new FlightResponseDTO();
        FlightResponseDTO flight2 = new FlightResponseDTO();
        List<FlightResponseDTO> flights = List.of(flight1, flight2);

        Mockito.when(flightService.getAllFlights()).thenReturn(flights);

        String expectedJson = objectMapper.writeValueAsString(flights);

        // when + then
        mockMvc.perform(get("/api/flight"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(flightService, times(1)).getAllFlights();
    }

    @Test
    void searchFlights_shouldReturnMatchingFlights() throws Exception {
        // given
        String source = "DEL";
        String destination = "BOM";
        String date = "2025-12-31";

        FlightResponseDTO flight = new FlightResponseDTO();
        List<FlightResponseDTO> flights = List.of(flight);

        Mockito.when(flightService.searchFlights(any()))
                .thenReturn(flights);

        String expectedJson = objectMapper.writeValueAsString(flights);

        // when + then
        mockMvc.perform(
                        get("/api/flight/search")
                                .param("source", source)
                                .param("destination", destination)
                                .param("date", date)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(flightService, times(1)).searchFlights(any());
    }

    @Test
    void getFlightByNumber_shouldReturnSingleFlight() throws Exception {
        // given
        String flightNumber = "AI101";

        FlightResponseDTO flight = new FlightResponseDTO();
        Mockito.when(flightService.getFlightByNumber(flightNumber))
                .thenReturn(flight);

        String expectedJson = objectMapper.writeValueAsString(flight);

        // when + then
        mockMvc.perform(get("/api/flight/{flightNumber}", flightNumber))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(flightService, times(1)).getFlightByNumber(flightNumber);
    }
}
