package com.eventure.events.Services;

import com.eventure.events.dto.Ticket;
import com.eventure.events.model.BookingDetails;
import com.eventure.events.repository.BookingRepo;
import com.eventure.events.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.json.JSONObject;

import com.eventure.events.exception.MyException;
import com.eventure.events.model.Events;
import com.eventure.events.repository.EventRepo;
import com.eventure.events.dto.EventUpdateRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventServices {

    private BookingRepo bookingRepo;
    private EventRepo eventRepo;
    private UserRepo userRepo;

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;
    private final RestTemplate restTemplate = new RestTemplate();

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
        if (event.getAddress() == null || event.getCity() == null || event.getState() == null || event.getZipCode() == null) {
            throw new MyException("Address, city, state, and zip code is required.");
        }
        event.setEventAttendees(0);
        event.setAvailable_tickets(event.getEventCapacity());
        updateEventGeocoding(event);
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

    public Events updateEvent(String eventId, EventUpdateRequest updateRequest, String userId) {
        Events event = eventRepo.findById(eventId).orElseThrow(() -> new MyException("Event not found"));
        if (!event.getOrganizerId().equals(userId)) {
            throw new MyException("User not authorized to update this event");
        }
        // Only update allowed fields
        if (updateRequest.getDesc() != null) event.setDesc(updateRequest.getDesc());
        if (updateRequest.getTicketPrice() != null) event.setTicketPrice(updateRequest.getTicketPrice());
        if (updateRequest.getEventDateTime() != null) event.setEventDateTime(updateRequest.getEventDateTime());
        if (updateRequest.getCity() != null) event.setCity(updateRequest.getCity());
        if (updateRequest.getState() != null) event.setState(updateRequest.getState());
        if (updateRequest.getZipCode() != null) event.setZipCode(updateRequest.getZipCode());
        if (updateRequest.getAddress() != null) event.setAddress(updateRequest.getAddress());
        if (updateRequest.getEventInstruction() != null) event.setEventInstruction(updateRequest.getEventInstruction());
        // Handle eventCapacity and available_tickets logic
        if (updateRequest.getEventCapacity() != null) {
            int oldCapacity = event.getEventCapacity();
            int oldAvailable = event.getAvailable_tickets();
            int diff = updateRequest.getEventCapacity() - oldCapacity;
            int newAvailable = oldAvailable + diff;
            if (newAvailable < 0) newAvailable = 0;
            event.setEventCapacity(updateRequest.getEventCapacity());
            event.setAvailable_tickets(newAvailable);
        }
        // Google Maps Geocoding
        updateEventGeocoding(event);
        return eventRepo.save(event);
    }

    private void updateEventGeocoding(Events event) {
        try {
            String address = String.join(", ", event.getAddress(), event.getCity(), event.getState(), event.getZipCode());
            String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address.replace(" ", "+") + "&key=" + googleMapsApiKey;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JSONObject json = new JSONObject(response.getBody());
            if (json.has("results") && json.getJSONArray("results").length() > 0) {
                JSONObject locationJson = json.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                double lat = locationJson.getDouble("lat");
                double lng = locationJson.getDouble("lng");
                String gmapUrl = "https://www.google.com/maps/search/?api=1&query=" + address.replace(" ", "+");
                if (event.getLocation() == null) {
                    event.setLocation(new com.eventure.events.dto.Location());
                }
                event.getLocation().setLatitude(lat);
                event.getLocation().setLongitude(lng);
                event.getLocation().setGmapUrl(gmapUrl);
            }
        } catch (Exception e) {
            // Optionally log or handle geocoding errors
        }
    }

}

