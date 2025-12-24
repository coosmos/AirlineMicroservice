package com.app.feign;


import com.app.dto.FlightResponseDTO;
import com.app.entity.FlightWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.crypto.spec.PSource;
import java.time.LocalDate;
import java.util.List;

@FeignClient(
        name="flight-service",
        fallback = FlightClientFallback.class

)
public interface FlightClient {

    @GetMapping("/api/flight/search")
     List<FlightResponseDTO>  searchFlights(@RequestParam String source,
                                            @RequestParam String destination,
                                            @RequestParam LocalDate date
                                        );

    @GetMapping("/api/flight/{flightNumber}")
    FlightResponseDTO getFlightbyNumber(@PathVariable String flightNumber);


}
