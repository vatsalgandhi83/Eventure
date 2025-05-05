package com.eventure.events.Services;

import com.eventure.events.dto.Ticket;
import com.eventure.events.model.BookingDetails;
import com.eventure.events.repository.BookingRepo;
import com.eventure.events.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventure.events.exception.MyException;
import com.eventure.events.model.Events;
import com.eventure.events.repository.EventRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventServices {

    private BookingRepo bookingRepo;
    private EventRepo eventRepo;
    private UserRepo userRepo;

    @Autowired
    public EventServices(BookingRepo bookingRepo, EventRepo eventRepo, UserRepo userRepo) {
        this.bookingRepo = bookingRepo;
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
    }

    public Events createEvent(Events event) {
        if (!userRepo.existsById(event.getOrganizerId())) {
            throw new MyException("Organizer not found with ID: " + event.getOrganizerId());
        }
        if (event.getEventImageBase64() != null) {
            String base64Image = event.getEventImageBase64();
            int imageSizeBytes = (int) ((base64Image.length() * 3) / 4);
            if (imageSizeBytes > 1024 * 1024 * 2) {
                throw new MyException("Event banner image is too large. Max size allowed is 2MB.");
            }
        }
        event.setEventAttendees(0);
        return eventRepo.save(event);
    }

    public List<Events> getAllEvents() {
        return eventRepo.findAll();
    }

    public Optional<Events> getEventById(String id) {
        if (!eventRepo.existsById(id)) {
            throw new MyException("Event does not exist");
        }
        return eventRepo.findById(id);
    }

    public List<Events> getEventsByUserId(String userId) {
        List<BookingDetails> bookings = bookingRepo.findByUserIdAndBookingStatus(userId, "CONFIRMED");

        List<String> eventIds = new ArrayList<>();

        for (BookingDetails booking : bookings) {
            List<Ticket> tickets = booking.getTickets();
            for (Ticket ticket : tickets) {
                String eventId = ticket.getEventId();
                if (!eventIds.contains(eventId)) {
                    eventIds.add(eventId);
                }
            }
        }

        if (eventIds.isEmpty()) {
            return new ArrayList<>();
        }

        return eventRepo.findByIdIn(eventIds); // _id matching
    }

}

