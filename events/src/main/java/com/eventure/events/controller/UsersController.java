package com.eventure.events.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eventure.events.Services.UserService;
import com.eventure.events.model.Users;
import com.eventure.events.repository.UserRepo;

import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value = "/api/user")
public class UsersController {

	@Autowired
	UserRepo userRepo;

	@Autowired
	private UserService userService;

	/*
	 * @ApiIgnore
	 * 
	 * @RequestMapping(value = "/api/user") public void redirect(HttpServletResponse
	 * response) throws IOException { response.sendRedirect("/swagger-ui.html"); }
	 */

	@GetMapping
	public List<Users> getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Users> getUserById(@PathVariable String id) {
		Optional<Users> user = userService.getUserById(id);
		return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Users> createUser(@RequestBody Users user) {
		Users createUser = userService.addNewUser(user);
		return new ResponseEntity<>(createUser, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Users> updateUser(@PathVariable String id, @RequestBody Users user) {
		Users updatedUser = userService.updateUser(id, user);
		return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable String id) {
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}

}
