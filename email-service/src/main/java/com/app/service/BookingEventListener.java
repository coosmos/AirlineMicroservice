package com.app.service;

import com.app.event.BookingConfirmedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BookingEventListener {

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "booking-confirmed", groupId = "email-service-group")
    public void handleBookingConfirmed(BookingConfirmedEvent event) {
        System.out.println(" Kafka event received: Booking confirmed for PNR: " + event.getPnr());

        emailService.sendBookingConfirmationEmail(event);
    }
}