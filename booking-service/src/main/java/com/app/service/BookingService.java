package com.app.service;

import com.app.feign.FlightClient;
import com.app.dto.*;
import com.app.entity.Booking;
import com.app.entity.Passenger;
import com.app.event.BookingConfirmedEvent;
import com.app.event.PassengerInfo;
import com.app.event.SeatBookedEvent;
import com.app.exception.BookingNotFoundException;
import com.app.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private FlightClient flightClient;

    @Autowired
    private KafkaTemplate<String, SeatBookedEvent> seatBookedKafkaTemplate;

    @Autowired
    private KafkaTemplate<String, BookingConfirmedEvent> bookingConfirmedKafkaTemplate;

    // Create booking
    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO requestDTO) {

        // 1. Validate flight exists and has seats available (via Feign)
        FlightResponseDTO flight = flightClient.getFlightbyNumber(requestDTO.getFlightNumber());

        int requestedSeats = requestDTO.getPassengers().size();

        if (flight.getAvailableSeats() < requestedSeats) {
            throw new RuntimeException("Not enough seats available. Only "
                    + flight.getAvailableSeats() + " seats left.");
        }

        // 2. Generate unique PNR
        String pnr = generatePNR();

        // 3. Calculate total price
        Double totalPrice = flight.getPrice() * requestedSeats;

        // 4. Create Booking entity
        Booking booking = Booking.builder()
                .pnr(pnr)
                .flightNumber(requestDTO.getFlightNumber())
                .bookingDate(LocalDateTime.now())
                .status(Booking.BookingStatus.CONFIRMED)
                .totalPrice(totalPrice)
                .contactEmail(requestDTO.getContactEmail())
                .contactPhone(requestDTO.getContactPhone())
                .passengers(new ArrayList<>())
                .build();

        // 5. Create Passenger entities
        List<Passenger> passengers = requestDTO.getPassengers().stream()
                .map(passengerDTO -> Passenger.builder()
                        .firstName(passengerDTO.getFirstName())
                        .lastName(passengerDTO.getLastName())
                        .age(passengerDTO.getAge())
                        .gender(passengerDTO.getGender())
                        .email(passengerDTO.getEmail())
                        .phoneNumber(passengerDTO.getPhoneNumber())
                        .booking(booking)
                        .build())
                .collect(Collectors.toList());

        booking.setPassengers(passengers);

        // 6. Save booking
        Booking savedBooking = bookingRepository.save(booking);

        // 7. Publish SeatBookedEvent (for flight-service to reduce seats)
        SeatBookedEvent seatEvent = new SeatBookedEvent(
                requestDTO.getFlightNumber(),
                requestedSeats,
                pnr
        );
        seatBookedKafkaTemplate.send("seat-booked-topic", seatEvent);
        System.out.println(" SeatBookedEvent published: " + seatEvent);

        // 8. Publish BookingConfirmedEvent (for email-service to send email)
        List<PassengerInfo> passengerInfos = requestDTO.getPassengers().stream()
                .map(p -> new PassengerInfo(
                        p.getFirstName(),
                        p.getLastName(),
                        p.getAge(),
                        p.getGender()
                ))
                .collect(Collectors.toList());

        BookingConfirmedEvent confirmedEvent = new BookingConfirmedEvent(
                pnr,
                flight.getFlightNumber(),
                flight.getAirline(),
                flight.getSource(),
                flight.getDestination(),
                flight.getDepartureTime(),
                passengerInfos,
                totalPrice,
                requestDTO.getContactEmail(),
                requestDTO.getContactPhone()
        );

        bookingConfirmedKafkaTemplate.send("booking-confirmed", confirmedEvent);
        System.out.println(" BookingConfirmedEvent published for PNR: " + pnr);

        // 9. Return response
        return mapToResponseDTO(savedBooking);
    }

    // Get all bookings
    public List<BookingResponseDTO> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();

        return bookings.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    @Transactional
    public BookingResponseDTO cancelBooking(String pnr) {
        Booking booking = bookingRepository.findByPnr(pnr)
                .orElseThrow(() -> new BookingNotFoundException(
                        "Booking not found with PNR: " + pnr));

        if ("CANCELLED".equals(booking.getStatus())) {
            throw new RuntimeException("Booking already cancelled");
        }

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        Booking updatedBooking = bookingRepository.save(booking);

        // Publish Kafka event to release seats back  --TODO
        // (flight-service will listen and increase seat count)

        return mapToResponseDTO(updatedBooking);
    }

    // Get booking by PNR
    public BookingResponseDTO getBookingByPnr(String pnr) {
        Booking booking = bookingRepository.findByPnr(pnr)
                .orElseThrow(() -> new BookingNotFoundException(
                        "Booking not found with PNR: " + pnr));

        return mapToResponseDTO(booking);
    }

    // Helper: Generate random PNR
    private String generatePNR() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder pnr = new StringBuilder("PNR");

        for (int i = 0; i < 6; i++) {
            pnr.append(chars.charAt(random.nextInt(chars.length())));
        }

        return pnr.toString();
    }

    // Helper: Map Entity to DTO
    private BookingResponseDTO mapToResponseDTO(Booking booking) {

        List<PassengerDTO> passengerDTOs = booking.getPassengers().stream()
                .map(passenger -> new PassengerDTO(
                        passenger.getFirstName(),
                        passenger.getLastName(),
                        passenger.getAge(),
                        passenger.getGender(),
                        passenger.getEmail(),
                        passenger.getPhoneNumber()
                ))
                .collect(Collectors.toList());

        return BookingResponseDTO.builder()
                .id(booking.getId())
                .pnr(booking.getPnr())
                .flightNumber(booking.getFlightNumber())
                .passengers(passengerDTOs)
                .bookingDate(booking.getBookingDate())
                .status(booking.getStatus())
                .totalPrice(booking.getTotalPrice())
                .contactEmail(booking.getContactEmail())
                .contactPhone(booking.getContactPhone())
                .build();
    }
}