package com.eventure.events.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

import com.eventure.events.model.Users;

public interface UserRepo extends MongoRepository<Users, String>{
	Optional<Users> findByEmail(String email);

}