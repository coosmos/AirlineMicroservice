package com.app.amadeus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class AmadeusFlightService {

    @Value("${amadeus.base-url}")
    private String baseUrl;

    private final AmadeusAuthService authService;
    private final RestTemplate restTemplate = new RestTemplate();

    public AmadeusFlightService(AmadeusAuthService authService) {
        this.authService = authService;
    }

    public List<AmadeusFlightResponseDTO> searchFlightOffers(
            String origin,
            String destination,
            String departureDate
    ) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authService.getAccessToken());
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = baseUrl
                + "/v2/shopping/flight-offers"
                + "?originLocationCode=" + origin
                + "&destinationLocationCode=" + destination
                + "&departureDate=" + departureDate
                + "&adults=1";

        ResponseEntity<String> response =
                restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());
        JsonNode data = root.path("data");

        List<AmadeusFlightResponseDTO> flights = new ArrayList<>();

        for (JsonNode offer : data) {
            flights.add(mapToFlightResponse(offer));
        }

        return flights.stream().sorted(Comparator.comparingDouble(AmadeusFlightResponseDTO::getPrice))
                .limit(15)
                .toList();
    }

    private AmadeusFlightResponseDTO mapToFlightResponse(JsonNode offer) {
        AmadeusFlightResponseDTO dto = new AmadeusFlightResponseDTO();
        JsonNode itinerary = offer.path("itineraries").get(0);
        JsonNode segment = itinerary.path("segments").get(0);
        String carrierCode = segment.path("carrierCode").asText("");
        String flightNum = segment.path("number").asText("");
        dto.setFlightNumber(carrierCode.isEmpty() ? "" : carrierCode + "-" + flightNum);
        // airline name not available
        dto.setAirline("AIRLINE");
        dto.setSource(segment.path("departure").path("iataCode").asText(""));
        dto.setDestination(segment.path("arrival").path("iataCode").asText(""));
        dto.setDepartureTime(segment.path("departure").path("at").asText(""));
        dto.setArrivalTime(segment.path("arrival").path("at").asText(""));
        dto.setPrice(offer.path("price").path("total").asDouble(0.0));
        int seats = offer.path("numberOfBookableSeats").asInt(0);
        dto.setAvailableSeats(seats);
        dto.setTotalSeats(seats);
        return dto;
    }


}
