package com.eventure.events.dto;

import java.time.LocalDateTime;

public class EventUpdateRequest {
    private String desc;
    private Integer eventCapacity;
    private Float ticketPrice;
    private LocalDateTime eventDateTime;
    private String city;
    private String state;
    private String zipCode;
    private String address;
    private String eventInstruction;

    // Getters and setters
    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }
    public Integer getEventCapacity() { return eventCapacity; }
    public void setEventCapacity(Integer eventCapacity) { this.eventCapacity = eventCapacity; }
    public Float getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(Float ticketPrice) { this.ticketPrice = ticketPrice; }
    public LocalDateTime getEventDateTime() { return eventDateTime; }
    public void setEventDateTime(LocalDateTime eventDateTime) { this.eventDateTime = eventDateTime; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getEventInstruction() { return eventInstruction; }
    public void setEventInstruction(String eventInstruction) { this.eventInstruction = eventInstruction; }
} 