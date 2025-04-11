package com.eventure.events.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eventure.events.exception.MyException;
import com.eventure.events.model.Events;
import com.eventure.events.repository.EventRepo;
import java.util.List;
import java.util.Optional;

@Service
public class EventServices {

    @Autowired
    private EventRepo eventRepo;

    public List<Events> getAllEvents() {
        return eventRepo.findAll();
    }

	public Optional<Events> getEventById(String id) {
		if (!eventRepo.existsById(id)) {
			throw new MyException("Event does not exist");
		}
		
		return eventRepo.findById(id);
	}
	
}

