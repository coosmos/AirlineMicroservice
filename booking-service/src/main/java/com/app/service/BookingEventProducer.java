package com.app.service;

import com.app.event.SeatBookedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingEventProducer {

    private final KafkaTemplate<String, SeatBookedEvent> bookingEventKafkaTemplate;

    @Value("${app.kafka.topics.seat-booked}")
    private String bookingConfirmedTopic;

    public void sendBookingConfirmedEvent(SeatBookedEvent event) {
        // key = flightNumber (good for partitioning)
        bookingEventKafkaTemplate.send(bookingConfirmedTopic, event.getFlightNumber(), event);
    }
}
