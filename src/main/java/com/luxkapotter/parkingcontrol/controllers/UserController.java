package com.luxkapotter.parkingcontrol.controllers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.luxkapotter.parkingcontrol.models.User;
import com.luxkapotter.parkingcontrol.services.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	private UserService service;

	@PostMapping
	@ResponseBody
	public ResponseEntity<User> insert(@RequestBody User user) {
		user = service.insert(user);
		return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}
	
	@GetMapping
	@ResponseBody
	public ResponseEntity<Page<User>> findAll(
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
		Page<User> list = service.findAll(pageable);
		return new ResponseEntity<Page<User>>(list, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@ResponseBody
	public ResponseEntity<Object> findById(@PathVariable(value = "id") UUID id) {
		Optional<User> userOptional = service.findById(id);
		if (!userOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
		} else {
			userOptional.get();
		}
		return new ResponseEntity<Object>(userOptional, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity<String> delete(@PathVariable(value = "id") UUID id) {
		Optional<User> userOptional = service.findById(id);
		if (!userOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
		}
		service.delete(userOptional.get());
		return new ResponseEntity<String>("Deleted Successfully!", HttpStatus.NO_CONTENT);
	}
	
	@PutMapping("/{id}")
	@ResponseBody
	public ResponseEntity<Object> update(@PathVariable(value = "id") UUID id,
			@RequestBody User user) {
		Optional<User> userOptional = service.findById(id);
		if (!userOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
		}
		user = service.update(id, user);
		return new ResponseEntity<Object>(user, HttpStatus.OK);
	}
}
