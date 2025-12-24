package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightSearchDTO {
    private String source;
    private String destination;
    private String date; // Format: "2025-12-25"
}