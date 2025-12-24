package com.app.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FlightNotFoundExceptionTest {

    @Test
    void shouldStoreMessage() {
        FlightNotFoundException ex = new FlightNotFoundException("Flight not found");

        assertThat(ex.getMessage()).isEqualTo("Flight not found");
    }
}
