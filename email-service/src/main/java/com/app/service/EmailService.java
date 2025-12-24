package com.app.service;

import com.app.event.BookingConfirmedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendBookingConfirmationEmail(BookingConfirmedEvent event) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(event.getContactEmail());
            message.setSubject("Booking Confirmed");

            message.setText("Hey! Your flight booking is confirmed. "
                    + "Here is your PNR: " + event.getPnr()
                    + ". Have a great trip :)");

            mailSender.send(message);

            System.out.println("Email sent to: " + event.getContactEmail());

        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
