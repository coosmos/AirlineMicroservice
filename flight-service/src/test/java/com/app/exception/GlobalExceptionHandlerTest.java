package com.app.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setup() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleValidationExceptions_shouldReturn400() {
        // mock field errors
        FieldError fieldError = new FieldError("obj", "flightNumber", "Invalid flight number");
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Map<String, Object>> response = handler.handleValidationExceptions(ex);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);

        Map<String, Object> body = response.getBody();
        assertThat(body).containsKeys("errors", "timestamp", "status");

        Map<String, String> errors = (Map<String, String>) body.get("errors");
        assertThat(errors.get("flightNumber")).isEqualTo("Invalid flight number");
    }

    @Test
    void handleFlightNotFound_shouldReturn404() {
        FlightNotFoundException ex = new FlightNotFoundException("No flight found");

        ResponseEntity<Map<String, Object>> response =
                handler.handleFlightNotFoundException(ex);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);

        Map<String, Object> body = response.getBody();
        assertThat(body.get("message")).isEqualTo("No flight found");
        assertThat(body.get("status")).isEqualTo(404);
    }

    @Test
    void handleGlobalException_shouldReturn500() {
        Exception ex = new Exception("Something went wrong");

        ResponseEntity<Map<String, Object>> response =
                handler.handleGlobalException(ex);

        assertThat(response.getStatusCodeValue()).isEqualTo(500);

        Map<String, Object> body = response.getBody();
        assertThat(body.get("message")).isEqualTo("Something went wrong");
        assertThat(body.get("status")).isEqualTo(500);
    }
}
