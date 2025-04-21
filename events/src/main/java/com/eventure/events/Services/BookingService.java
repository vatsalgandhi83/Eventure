package com.eventure.events.Services;

import com.eventure.events.dto.BookingRequest;
import com.eventure.events.dto.BookingResponse;
import com.eventure.events.dto.Ticket;
import com.eventure.events.exception.MyException;
import com.eventure.events.model.BookingDetails;
import com.eventure.events.model.Events;
import com.eventure.events.model.Users;
import com.eventure.events.repository.BookingRepo;
import com.eventure.events.repository.EventRepo;
import com.eventure.events.repository.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    private final BookingRepo bookingRepo;
    private final EventRepo eventRepo;
    private final UserRepo userRepo;

    @Autowired
    public BookingService(BookingRepo bookingRepo, EventRepo eventRepo, UserRepo userRepo) {
        this.bookingRepo = bookingRepo;
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
    }

    public BookingResponse bookEvent(BookingRequest request) {

         Users user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new MyException("User not found with id: " + request.getUserId()));

        Events event = eventRepo.findById(request.getEventId())
                .orElseThrow(() -> new MyException("Event not found with id: " + request.getEventId()));

        if (request.getTicketCount() > event.getAvailable_tickets()) {
            throw new MyException("Only " + event.getAvailable_tickets() + " tickets available, but "
                    + request.getTicketCount() + " requested.");
        }

        if (!request.isPaymentStatus()) {
            throw new MyException("Payment was not successful, booking aborted.");
        }

        List<Ticket> ticketList = new ArrayList<>();
        for (int i = 0; i < request.getTicketCount(); i++) {
            String ticketId = "T" + UUID.randomUUID().toString().substring(0, 8); // example: T8b2d4f1c
            Ticket ticket = new Ticket(ticketId, request.getTicketPrice(), request.getEventId());
            ticketList.add(ticket);
        }

        BookingDetails booking = new BookingDetails();
        booking.setUserId(request.getUserId());
        booking.setTicketCount(request.getTicketCount());
        booking.setTotalTicketPrice(request.getTotalTicketPrice());
        booking.setTickets(ticketList);
        booking.setBookingStatus("CONFIRMED");

        BookingDetails savedBooking = bookingRepo.save(booking);

        event.setAvailable_tickets(event.getAvailable_tickets() - request.getTicketCount());
        event.setEventAttendees(event.getEventAttendees()+request.getTicketCount());

        eventRepo.save(event);
        System.out.println("Booking Obj >>>>>>>>>>>>>>>>>>>>>>"+savedBooking);
        return new BookingResponse(savedBooking, user, event);
    }

    public String cancelBooking(String bookingId, String userId) {

        BookingDetails booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new MyException("Booking not found with id: " + bookingId));

        if (!booking.getUserId().equals(userId)) {
            throw new MyException("This booking does not belong to the user: " + userId);
        }

        if ("CANCELLED".equalsIgnoreCase(booking.getBookingStatus())) {
            throw new MyException("Booking is already cancelled.");
        }

        booking.setBookingStatus("CANCELLED");
        bookingRepo.save(booking);

        String eventId = booking.getTickets().get(0).getEventId();
        Events event = eventRepo.findById(eventId)
                .orElseThrow(() -> new MyException("Event not found with id: " + eventId));

        event.setAvailable_tickets(event.getAvailable_tickets() + booking.getTicketCount());
        event.setEventAttendees(Math.max(0, event.getEventAttendees() - booking.getTicketCount()));

        eventRepo.save(event);

        return "Booking cancelled successfully.";
    }

}

