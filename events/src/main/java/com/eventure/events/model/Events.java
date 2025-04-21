package com.eventure.events.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.eventure.events.dto.Location;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "EventDetails")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Events{
    @Id
    private String id;
    private String eventName;
    private String desc;
    private String organizerId;
    private int eventCapacity;
    private int available_tickets;
    private float ticketPrice;
    private LocalDateTime eventDateTime;
    private Location location;
    private String city;
    private String state;
    private String zipCode;
    private String address;
    private String eventInstruction;
    private String eventCategory;
    private int eventAttendees;

    public int getAvailable_tickets() {
        return available_tickets;
    }

    public void setAvailable_tickets(int available_tickets) {
        this.available_tickets = available_tickets;
    }
}
