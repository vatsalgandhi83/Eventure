package com.eventure.events.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.eventure.events.exception.MyException;
import com.eventure.events.model.Users;
import com.eventure.events.repository.UserRepo;

import java.util.List;
import java.util.Optional;

@Service
@Validated
public class UserService {

	@Autowired
	private UserRepo userRepo;

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public List<Users> getAllUsers() {
		return userRepo.findAll();
	}

	public Optional<Users> getUserById(String id) {
		if (!userRepo.existsById(id)) {
			throw new MyException("User does not exist");
		}
		return userRepo.findById(id);
	}

/*	// Move this api to AuthService or update the code for authentication
	public Users addNewUser(Users user) {
		System.out.println("New User Sign up request =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + user);
		Optional<Users> existingUser = userRepo.findByEmail(user.getEmail());
		if (existingUser.isPresent()) {
			throw new MyException("Email " + user.getEmail() + " is already registered.");
		}

		// user.setPassword(passwordEncoder.encode(user.getPassword()));

		return userRepo.save(user);
	}

	// Move this api to AuthService or update the code for authentication
	public Users updateUser(String id, Users user) {
		if (userRepo.existsById(id)) {
			user.setId(id);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			return userRepo.save(user);
		}
		return null;
	}

	// Move this api to AuthService or update the code for authentication
	public void deleteUser(String id) {
		userRepo.deleteById(id);
	}*/

}
