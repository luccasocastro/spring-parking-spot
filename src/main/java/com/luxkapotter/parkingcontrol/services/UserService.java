package com.luxkapotter.parkingcontrol.services;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.luxkapotter.parkingcontrol.models.User;
import com.luxkapotter.parkingcontrol.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;
	
	public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

	@Transactional
	public User insert(User user) {
		user.setPassword(passwordEncoder().encode(user.getPassword()));
		return repository.save(user);
	}

	public Page<User> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Optional<User> findById(UUID id) {
		return repository.findById(id);
	}

	@Transactional
	public void delete(User user) {
		repository.delete(user);
	}

	public User update(UUID id, User obj) {
		User entity = repository.getReferenceById(id);
		updateData(entity, obj);
		return repository.save(entity);

	}

	private void updateData(User entity, User obj) {
		entity.setUsername(obj.getUsername());
		entity.setPassword(new BCryptPasswordEncoder().encode(obj.getPassword()));
	}
}
