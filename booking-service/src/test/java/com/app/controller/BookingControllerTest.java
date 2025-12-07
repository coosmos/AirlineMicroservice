//package com.app.controller;
//
//import com.app.dto.BookingRequestDTO;
//import com.app.dto.BookingResponseDTO;
//import com.app.service.BookingService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(BookingController.class)
//class BookingControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private BookingService bookingService;
//
//    @Test
//    void testCreateBooking() throws Exception {
//        BookingResponseDTO mockResponse = new BookingResponseDTO();
//        mockResponse.setPnr("XYZ123");
//
//        when(bookingService.createBooking(any())).thenReturn(mockResponse);
//
//        mockMvc.perform(post("/api/bookings")
//                        .content("""
//                        {
//                          "flightId": 1,
//                          "userId": 5
//                        }
//                        """)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.pnr").value("XYZ123"));
//    }
//
//    @Test
//    void testGetAllBookings() throws Exception {
//        BookingResponseDTO b1 = new BookingResponseDTO();
//        b1.setPnr("PNR1");
//
//        BookingResponseDTO b2 = new BookingResponseDTO();
//        b2.setPnr("PNR2");
//
//        when(bookingService.getAllBookings()).thenReturn(List.of(b1, b2));
//
//        mockMvc.perform(get("/api/bookings"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(2))
//                .andExpect(jsonPath("$[0].pnr").value("PNR1"));
//    }
//
//    @Test
//    void testGetBookingByPnr() throws Exception {
//        BookingResponseDTO mockBooking = new BookingResponseDTO();
//        mockBooking.setPnr("ABC123");
//
//        when(bookingService.getBookingByPnr("ABC123")).thenReturn(mockBooking);
//
//        mockMvc.perform(get("/api/bookings/ABC123"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.pnr").value("ABC123"));
//    }
//
//    @Test
//    void testCancelBooking() throws Exception {
//        BookingResponseDTO mockResponse = new BookingResponseDTO();
//        mockResponse.setPnr("CANCEL123");
//
//        when(bookingService.cancelBooking("CANCEL123")).thenReturn(mockResponse);
//
//        mockMvc.perform(put("/api/bookings/CANCEL123/cancel"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.pnr").value("CANCEL123"));
//    }
//
//    @Test
//    void testHealthCheck() throws Exception {
//        mockMvc.perform(get("/api/bookings/healthCheck"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("OK"));
//    }
//}
