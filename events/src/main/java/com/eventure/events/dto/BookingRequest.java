package com.eventure.events.dto;

public class BookingRequest {

    private String userId;
    private String eventId;
    private int ticketCount;
    private double ticketPrice;
    private double totalTicketPrice;
    private boolean paymentStatus;  // true if payment is successful

    // Getters and setters (or use Lombok @Data for brevity)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public int getTicketCount() {
        return ticketCount;
    }

    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public double getTotalTicketPrice() {
        return totalTicketPrice;
    }

    public void setTotalTicketPrice(double totalTicketPrice) {
        this.totalTicketPrice = totalTicketPrice;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}

