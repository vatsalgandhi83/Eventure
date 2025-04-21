package com.eventure.events.dto;

import com.eventure.events.model.BookingDetails;
import com.eventure.events.model.Events;
import com.eventure.events.model.Users;

public class BookingResponse {
    private String bookingId;
    private int ticketCount;
    private double totalTicketPrice;
    private String bookingStatus;
    private BookingDetails booking;
    private Users user;
    private Events event;

    public BookingResponse(BookingDetails booking, Users user, Events event) {
        this.bookingId = booking.getId();
        this.ticketCount = booking.getTicketCount();
        this.totalTicketPrice = booking.getTotalTicketPrice();
        this.bookingStatus = booking.getBookingStatus();
        this.booking = booking;
        this.user = user;
        this.event = event;
    }

    public String getBookingId() { return bookingId; }
    public int getTicketCount() { return ticketCount; }
    public double getTotalTicketPrice() { return totalTicketPrice; }
    public String getBookingStatus() { return bookingStatus; }
    public BookingDetails getBooking() { return booking; }
    public Users getUser() { return user; }
    public Events getEvent() { return event; }
}
