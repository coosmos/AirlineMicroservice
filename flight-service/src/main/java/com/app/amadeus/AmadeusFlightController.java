package com.app.amadeus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AmadeusFlightController {
    @Autowired
    private  AmadeusAuthService authService;
    @Autowired
    private  AmadeusFlightService flightService;
    public AmadeusFlightController(AmadeusAuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/amadeus/token")
    public String getToken() {
        return authService.getAccessToken();
    }



//    @GetMapping("/api/flight/offers")
//    public List<AmadeusFlightResponseDTO> offers(
//            @RequestParam String origin,
//            @RequestParam String destination,
//            @RequestParam String date
//    ) throws Exception {
//        return flightService.searchFlightOffers(origin, destination, date);
//    }

}
