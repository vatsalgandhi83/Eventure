package com.eventure.events.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.eventure.events.model.Events;
import java.util.List;

@Repository
public interface EventRepo extends MongoRepository<Events, String> {
    List<Events> findByIdIn(List<String> ids);
}
