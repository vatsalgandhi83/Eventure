package com.eventure.events.controller;

import com.eventure.events.Services.BookingService;
import com.eventure.events.dto.BookingRequest;
import com.eventure.events.dto.BookingResponse;
import com.eventure.events.dto.CancelBookingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/bookEvent")
    public ResponseEntity<BookingResponse> bookEvent(@RequestBody BookingRequest request) {
        BookingResponse response = bookingService.bookEvent(request);
        return ResponseEntity.ok(response);
    }

  /*   @PostMapping("/cancelBooking")
    public ResponseEntity<String> cancelBooking(
            @RequestParam(required = true) String id,
            @RequestParam(required = false) String userId) {
        String message = bookingService.cancelBooking(id, userId);
        return ResponseEntity.ok(message);
    }*/

    @PostMapping("/cancelBooking")
public ResponseEntity<String> cancelBooking(@RequestBody CancelBookingRequest request) {
    String message = bookingService.cancelBooking(request.getBookingId(), request.getUserId());
    return ResponseEntity.ok(message);
}

}
