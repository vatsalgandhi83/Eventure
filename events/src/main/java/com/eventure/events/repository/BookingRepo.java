package com.eventure.events.repository;

import com.eventure.events.model.BookingDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface BookingRepo extends MongoRepository<BookingDetails, String> {
    List<BookingDetails> findByUserIdAndBookingStatus(String userId, String bookingStatus);
}