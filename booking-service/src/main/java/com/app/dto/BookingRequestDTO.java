package com.app.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDTO {

    @NotBlank(message = "Flight number is required")
    private String flightNumber;

    @NotEmpty(message = "At least one passenger is required")
    @Valid
    private List<PassengerDTO> passengers;

    @NotBlank(message = "Contact email is required")
    @Email(message = "Invalid email format")
    private String contactEmail;

    @NotBlank(message = "Contact phone is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String contactPhone;
}