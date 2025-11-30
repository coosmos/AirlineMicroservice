package com.app.feign;


import com.app.entity.FlightWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class FlightClientFallback implements FlightClient {


    @Override
    public List<FlightWrapper> searchFlights(String source, String destination, LocalDate date) {
        return List.of();
    }

    @Override
    public FlightWrapper getFlightbyNumber(String flightNumber) {
        return null;
    }
}
