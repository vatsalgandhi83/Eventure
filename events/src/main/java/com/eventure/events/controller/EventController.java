package com.eventure.events.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eventure.events.Services.EventServices;
import com.eventure.events.model.Events;

@RestController
@RequestMapping(value = "/api/events")
public class EventController {
	
	  @Autowired
	    private EventServices eventService;

	@PostMapping("/createEvent")
	public ResponseEntity<Events> createEvent(@RequestBody Events event) {
		Events created = eventService.createEvent(event);
		return ResponseEntity.ok(created);
	}

	    @GetMapping
	    public List<Events> getAllEvents() {
	        return eventService.getAllEvents();
	    }
	    
	    @GetMapping("/{event_id}")
		public ResponseEntity<Events> getEventById(@PathVariable String event_id) {
			Optional<Events> event = eventService.getEventById(event_id);
			return event.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
		}

	@GetMapping("/byUser")
	public ResponseEntity<List<Events>> getEventsByUserId(@RequestParam String userId) {
		List<Events> userEvents = eventService.getEventsByUserId(userId);
		return ResponseEntity.ok(userEvents);
	}
}
